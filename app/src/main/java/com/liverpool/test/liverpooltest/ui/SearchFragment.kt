package com.liverpool.test.liverpooltest.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.liverpool.test.liverpool.Constants
import com.liverpool.test.liverpooltest.R
import com.liverpool.test.liverpooltest.databinding.FragmentSearchBinding
import com.liverpool.test.liverpooltest.repository.network.Connection
import com.liverpool.test.liverpooltest.repository.network.json.response.Records
import com.liverpool.test.liverpooltest.ui.dialog.CloseListener
import com.liverpool.test.liverpooltest.ui.dialog.DialogMessage
import com.liverpool.test.liverpooltest.ui.adapter.ItemRecordListener
import com.liverpool.test.liverpooltest.ui.adapter.SearcherAdapter
import com.liverpool.test.liverpooltest.viewModel.SearcherViewModel

class SearchFragment : Fragment() {
    private lateinit var _binding: FragmentSearchBinding
    val binding: FragmentSearchBinding get() = _binding

    private val searcherViewModel: SearcherViewModel by activityViewModels()

    private lateinit var adapter: SearcherAdapter

    private var currentAnimator: Animator? = null
    private var shortAnimationDuration: Int = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner

        adapter = SearcherAdapter(ItemRecordListener { records,imageView -> actionOnClick(records,imageView) })
        makeApiCall()
        event()
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        return binding.root
    }

    private fun event(){
        binding.searchImageview.setOnClickListener {
            getAlLRecords()
        }
        binding.searcher.setOnKeyListener{ view, keyCode,_  ->handleKeyEvent(view,keyCode)}
        binding.back.setOnClickListener{
            searcherViewModel.onBackPage()
        }
        binding.next.setOnClickListener{
            searcherViewModel.onNextPage()
        }
    }

    private fun getAlLRecords(){
        val serachString = binding.searcher.text.toString()
        if( serachString != ""){
            searcherViewModel.saveSearch(serachString)
            searcherViewModel.getPlpStates(serachString)
        }
        else{
            val dialog = DialogMessage(getString(R.string.invalidString), CloseListener {restartStatus()})
            dialog.show(requireActivity().supportFragmentManager, Constants.DIALOG_TAG)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searcherRecycler.adapter = adapter
        binding.searcherRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.searcherRecycler.setHasFixedSize(false)
    }

    private fun actionOnClick(records: Records, imageView: ImageView) {
        zoomImageFromThumb(imageView, records)
    }



    private fun makeApiCall() {
        searcherViewModel.allRecords.observe(requireActivity(),{
            it.let {
                adapter.submitList(it)
            }
        })
        searcherViewModel.pettionstatus.observe(requireActivity(),{
            it.let {
                showDialog(it)
            }
        })
    }

    private fun showDialog(status : Int){
        when(status){
            Constants.pettionStatus.ERROR_REQUEST -> {
                val dialog = DialogMessage(getString(R.string.error_message), CloseListener {restartStatus()})
                dialog.show(requireActivity().supportFragmentManager, Constants.DIALOG_TAG)
                hiddenPage()
            }
            Constants.pettionStatus.WITHOUTINTERNET -> {
                val dialog = DialogMessage(getString(R.string.without__internet), CloseListener {restartStatus()})
                dialog.show(requireActivity().supportFragmentManager, Constants.DIALOG_TAG)
                hiddenPage()
            }
            Constants.pettionStatus.WITHOUT_RESULT -> {
                val dialog = DialogMessage(getString(R.string.without_result), CloseListener {restartStatus()})
                dialog.show(requireActivity().supportFragmentManager, Constants.DIALOG_TAG)
                hiddenPage()
            }
            Constants.pettionStatus.SUCCESS -> {
                showPage()
            }
            else->{}
        }

    }


    private fun showPage(){
        if (searcherViewModel.existBack()){
            binding.back.visibility = View.VISIBLE
        }
        else{
            binding.back.visibility = View.INVISIBLE
        }

        if(searcherViewModel.existNext()){
            binding.next.visibility = View.VISIBLE
        }
        else{
            binding.next.visibility = View.INVISIBLE
        }

        binding.concurrent.visibility = View.VISIBLE
        binding.concurrent.text = "${searcherViewModel.page}...${searcherViewModel.maxPage}"
    }

    fun hiddenPage(){
        binding.concurrent.visibility = View.INVISIBLE
        binding.back.visibility = View.INVISIBLE
        binding.next.visibility = View.INVISIBLE
    }


    private fun restartStatus(){
        searcherViewModel.resetPetitionStatus()
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }

    private fun zoomImageFromThumb(thumbView: View, records: Records){
        currentAnimator?.cancel()

        val expandedImageView = binding.expandedImage
        setImage(records.smImage)
        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()
        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBoundsInt)
        binding.container
            .getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)
        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            // Extend start bounds horizontally
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            // Extend start bounds vertically
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }
        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.alpha = 0f
        expandedImageView.visibility = View.VISIBLE
        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.pivotX = 0f
        expandedImageView.pivotY = 0f

        // Construct and run the parallel animation of the four translation and

        currentAnimator = AnimatorSet().apply {
            play(ObjectAnimator.ofFloat(
                expandedImageView,
                View.X,
                startBounds.left,
                finalBounds.left)
            ).apply {
                with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top, finalBounds.top))
                with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f))
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                }
            })
            start()
        }


        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        expandedImageView.setOnClickListener {
            currentAnimator?.cancel()

            // Animate the four positioning/sizing properties in parallel,
            // back to their original values.
            currentAnimator = AnimatorSet().apply {
                play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left)).apply {
                    with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
                    with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale))
                    with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale))
                }
                duration = shortAnimationDuration.toLong()
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        thumbView.alpha = 1f
                        expandedImageView.visibility = View.GONE
                        currentAnimator = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        thumbView.alpha = 1f
                        expandedImageView.visibility = View.GONE
                        currentAnimator = null
                    }
                })
                start()
            }
        }
    }


    private fun setImage(value : String){
        if(Connection.isConnected(requireContext())){
            Glide.with(requireContext()).load(value).into(binding.expandedImage)
        }
        else{
            binding.expandedImage.setImageResource(R.drawable.ic_baseline_wifi_off_24)
        }
    }

}