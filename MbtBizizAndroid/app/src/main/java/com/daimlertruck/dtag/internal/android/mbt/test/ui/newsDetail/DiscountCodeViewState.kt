package com.daimlertruck.dtag.internal.android.mbt.test.ui.newsDetail

data class DiscountCodeViewState(
    val discountCodeType: Int,
    val discountCode: String?
) {

    fun getDiscountLayoutVisibility(): Boolean {
        return discountCodeType == FOR_ALL_USER || discountCodeType == PERSONALIZED_CODE
    }

    fun getDiscountCodeLabelVisibility(): Boolean {
        return discountCode.isNullOrBlank().not()
    }

    fun getDiscountLabelVisibility(): Boolean {
        return (discountCodeType == PERSONALIZED_CODE && discountCode == null)
    }

    fun getNoDiscountLeftLabelVisibility(): Boolean {
        return discountCodeType == FOR_ALL_USER && discountCode == null
    }

    companion object {
        const val FOR_ALL_USER = 1
        const val PERSONALIZED_CODE = 2
    }
}