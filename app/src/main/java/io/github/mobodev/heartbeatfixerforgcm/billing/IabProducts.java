package io.github.mobodev.heartbeatfixerforgcm.billing;

import java.util.Arrays;
import java.util.List;

/**
 * Created by shaobin on 3/15/15.
 */
public class IabProducts {
    private IabProducts() {}

    public static final String PRODUCT_COFFEE = "coffee";
    public static final String PRODUCT_BEER = "beer";
    public static final String PRODUCT_HAMBURGER = "hamburger";
    public static final String PRODUCT_CAKE = "cake";

//    public static final String PRODUCT_TEST_PURCHASED = "android.test.purchased";

    public static final List<String> PRODUCT_LIST = Arrays.asList(PRODUCT_COFFEE,
            PRODUCT_BEER, PRODUCT_HAMBURGER, PRODUCT_CAKE);
}
