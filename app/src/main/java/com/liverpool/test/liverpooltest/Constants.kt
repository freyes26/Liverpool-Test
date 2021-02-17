 package com.liverpool.test.liverpool

 object Constants {

     const val IS_DATABASE = "isDatabase"
     const val IS_RETROFIT = "isRetrofit"

    const val LOG_TAG = "LIVERPOOL"
    const val DIALOG_TAG = "Dialog_tag"

     object retrofitConstants {
         const val BASE_URL = "https://shoppapp.liverpool.com.mx/"
     }

     object databseConstants {
         const val DATABASE_NAME = "Search_Database"
         const val SEARCH_TABLE = "Search"
     }

    object parameters {
        const val FORCE_PHP = "force-plp"
        const val SEARCH_STRING = "search-string"
        const val PAGE_NUMBER = "page-number"
        const val ITEMS_PER_PAGE = "number-of-items-per-page"
    }

    object pettionStatus{
        const val WITHOUT_STATUS = 0
        const val ERROR_REQUEST = 1
        const val WITHOUTINTERNET = 2
        const val SUCCESS = 3
        const val WITHOUT_RESULT = 4
    }
}
