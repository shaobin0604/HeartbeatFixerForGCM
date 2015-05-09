package io.github.mobodev.heartbeatfixerforgcm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.BillingRequests;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.ProductTypes;
import org.solovyev.android.checkout.Purchase;
import org.solovyev.android.checkout.RequestListener;
import org.solovyev.android.checkout.ResponseCodes;
import org.solovyev.android.checkout.Sku;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import io.github.mobodev.heartbeatfixerforgcm.billing.IabProducts;
import io.github.mobodev.heartbeatfixerforgcm.ui.activities.ActivityBase;
import io.github.mobodev.heartbeatfixerforgcm.ui.adapters.BetterArrayAdapter;
import io.github.mobodev.heartbeatfixerforgcm.utils.ToastUtils;
import io.github.mobodev.heartbeatfixerforgcm.utils.ViewUtils;

import static io.github.mobodev.heartbeatfixerforgcm.BuildConfig.DEBUG;

/**
 * Created by shaobin on 3/14/15.
 */
public class DonateDialogFragment extends DialogFragment {

    private static final String TAG = "DonateFragment";

    private static final int SCREEN_LOADING = 1;
    private static final int SCREEN_INVENTORY = 2;
    private static final int SCREEN_EMPTY_VIEW = 4;

    private TextView mEmptyView;
    private ProgressBar mProgressBar;

    private Inventory mInventory;
    private ActivityCheckout mCheckout;
    private final PurchaseListener mPurchaseListener = new PurchaseListener();
    private final InventoryLoadedListener mInventoryLoadedListener = new InventoryLoadedListener();

    private SkusListAdapter mAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ActivityBase) {
            ActivityBase ma = (ActivityBase) activity;
            mCheckout = ma.getCheckout();

            if (mCheckout == null) {
                throw new RuntimeException("You must call #requestCheckout() on the activity before!");
            }
        } else {
            throw new RuntimeException("Host activity must be an " +
                    "instance of ActivityBase.class!");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInventory = mCheckout.loadInventory();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.donate_dialog_title);

        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frameLayout = new FrameLayout(getActivity()); //

        View view = inflater.inflate(R.layout.dialog_donate_list, frameLayout, false);

        ListView gv = (ListView) view.findViewById(R.id.list);

        // Init description message.
        TextView textView = (TextView) inflater.inflate(R.layout.dialog_message, gv, false);
        textView.setText(R.string.donate_dialog_message);
        textView.setPadding(0, textView.getPaddingTop(), 0, textView.getPaddingBottom() / 2);

        // Init view with error view and progressbar-s.
        View phView = inflater.inflate(R.layout.dialog_donate_placeholder, gv, false);
        mProgressBar = (ProgressBar) phView.findViewById(R.id.progress);
        mEmptyView = (TextView) phView.findViewById(R.id.empty);

        gv.addHeaderView(textView, null, false);
        gv.addHeaderView(phView, null, false);
        gv.setAdapter(mAdapter = new SkusListAdapter(getActivity(), R.layout.sku_item));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SkuUi skuUi = (SkuUi) parent.getAdapter().getItem(position);
                purchase(skuUi.sku);
            }
        });
        builder.setView(view);
        builder.setNeutralButton(R.string.close, null);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        mCheckout.createPurchaseFlow(mPurchaseListener);
        reloadInventory();
    }

    @Override
    public void onStop() {
        mCheckout.destroyPurchaseFlow();
        super.onStop();
    }

    private void showScene(int visibility) {
        ViewUtils.setVisible(mProgressBar, visibility == SCREEN_LOADING);
        ViewUtils.setVisible(mEmptyView, visibility == SCREEN_EMPTY_VIEW);
    }

    private void reloadInventory() {
        showScene(SCREEN_LOADING);
        mInventory.load().whenLoaded(mInventoryLoadedListener);
    }

    private void purchase(@NonNull final Sku sku) {
        if (DEBUG) Log.d(TAG, "Purchasing " + sku.toString() + "...");
        mCheckout.whenReady(new Checkout.ListenerAdapter() {
            @Override
            public void onReady(@NonNull BillingRequests requests) {
                requests.purchase(sku, null, mCheckout.getPurchaseFlow());
            }
        });
    }

    private class InventoryLoadedListener implements Inventory.Listener {

        @Override
        public void onLoaded(@NonNull Inventory.Products products) {
            final Inventory.Product product = products.get(ProductTypes.IN_APP);
            mAdapter.setNotifyOnChange(false);
            mAdapter.clear();

            if (product.supported) {
                for (Sku sku : product.getSkus()) {
                    final Purchase purchase = product.getPurchaseInState(sku, Purchase.State.PURCHASED);
                    final SkuUi skuUi = new SkuUi(sku, purchase != null);
                    mAdapter.add(skuUi);
                }

                // Sort items by prices.
                mAdapter.sort(new Comparator<SkuUi>() {
                    @Override
                    public int compare(@NonNull SkuUi l, @NonNull SkuUi r) {
                        return (int) (l.sku.detailedPrice.amount - r.sku.detailedPrice.amount);
                    }
                });
                showScene(SCREEN_INVENTORY);
            } else {
                mEmptyView.setText(R.string.donate_billing_not_supported);
                showScene(SCREEN_EMPTY_VIEW);
            }

            mAdapter.notifyDataSetChanged();
        }

    }

    private abstract class BaseRequestListener<T> implements RequestListener<T> {

        @Override
        public void onError(int response, @NonNull Exception e) {
            if (Build.DEBUG) {
                ToastUtils.showShort(getActivity(), String.valueOf(response));
            }
        }

    }

    private class PurchaseListener extends BaseRequestListener<Purchase> {

        @Override
        public void onSuccess(@NonNull Purchase purchase) {
            purchased();
        }

        @Override
        public void onError(int response, @NonNull Exception e) {
            switch (response) {
                case ResponseCodes.ITEM_ALREADY_OWNED:
                    purchased();
                    break;
                default:
                    super.onError(response, e);
            }
        }

        private void purchased() {
            reloadInventory();
            ToastUtils.showLong(getActivity(), R.string.donate_thanks);
        }

    }

    private static class SkusListAdapter extends BetterArrayAdapter<SkuUi> {

        private static final class ViewHolder extends BetterArrayAdapter.ViewHolder {

            @NonNull
            private final ImageView image;

            @NonNull
            private final TextView description;

            @NonNull
            private final TextView price;

            @NonNull
            private final TextView currency;

            @NonNull
            private final TextView done;

            public ViewHolder(@NonNull View view) {
                super(view);
                image = (ImageView) view.findViewById(R.id.image);
                description = (TextView) view.findViewById(R.id.description);
                price = (TextView) view.findViewById(R.id.price);
                currency = (TextView) view.findViewById(R.id.currency);
                done = (TextView) view.findViewById(R.id.done);
            }
        }

        private Map<String, Integer> mImageResIdMap;

        public SkusListAdapter(@NonNull Context context, @LayoutRes int layoutRes) {
            super(context, layoutRes);
            initProductsImageResIdMap();
        }

        private void initProductsImageResIdMap() {
            mImageResIdMap = new HashMap<String, Integer>(IabProducts.PRODUCT_LIST.size());
            mImageResIdMap.put(IabProducts.PRODUCT_COFFEE, R.drawable.ic_coffee);
            mImageResIdMap.put(IabProducts.PRODUCT_BEER, R.drawable.ic_beer);
            mImageResIdMap.put(IabProducts.PRODUCT_HAMBURGER, R.drawable.ic_hamburger);
            mImageResIdMap.put(IabProducts.PRODUCT_CAKE, R.drawable.ic_cake);
        }

        @NonNull
        @Override
        protected ViewHolder onCreateViewHolder(@NonNull View view) {
            return new ViewHolder(view);
        }

        @Override
        protected void onBindViewHolder(@NonNull BetterArrayAdapter.ViewHolder holder, int i) {
            fill((ViewHolder) holder, getItem(i));
        }

        private void fill(@NonNull ViewHolder holder, @NonNull SkuUi skuUi) {
            holder.description.setText(skuUi.getDescription());

            int visibility;
            if (skuUi.isPurchased()) {
                visibility = View.GONE;
                holder.done.setVisibility(View.VISIBLE);
            } else {
                visibility = View.VISIBLE;
                holder.price.setText(skuUi.getPriceAmount());
                holder.currency.setText(skuUi.getPriceCurrency());
                holder.done.setVisibility(View.GONE);
            }

            holder.price.setVisibility(visibility);
            holder.currency.setVisibility(visibility);

            setImageResId(holder, skuUi.sku.id);
        }

        private void setImageResId(@NonNull ViewHolder holder, String productId) {
            Integer drawableResId = mImageResIdMap.get(productId);
            if (drawableResId == null) {
                drawableResId = 0;
            }
            holder.image.setImageResource(drawableResId);
        }

    }

    private static class SkusAdapter extends BetterArrayAdapter<SkuUi> {

        private static final class ViewHolder extends BetterArrayAdapter.ViewHolder {

            @NonNull
            private final android.widget.TextView description;

            @NonNull
            private final android.widget.TextView price;

            @NonNull
            private final android.widget.TextView currency;

            @NonNull
            private final ImageView done;

            public ViewHolder(@NonNull View view) {
                super(view);
                description = (android.widget.TextView) view.findViewById(R.id.description);
                View layout = view.findViewById(R.id.cost);
                price = (android.widget.TextView) layout.findViewById(R.id.price);
                currency = (android.widget.TextView) layout.findViewById(R.id.currency);
                done = (ImageView) layout.findViewById(R.id.done);
            }

        }

        public SkusAdapter(@NonNull Context context, @LayoutRes int layoutRes) {
            super(context, layoutRes);
        }

        @NonNull
        @Override
        public BetterArrayAdapter.ViewHolder onCreateViewHolder(@NonNull View view) {
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BetterArrayAdapter.ViewHolder viewHolder, int i) {
            fill(mContext, (ViewHolder) viewHolder, getItem(i));
        }

        private static void fill(@NonNull Context context,
                                 @NonNull ViewHolder holder,
                                 @NonNull SkuUi skuUi) {
            holder.description.setText(skuUi.getDescription());

            int visibility;
            if (skuUi.isPurchased()) {
                visibility = View.GONE;
                holder.done.setVisibility(View.VISIBLE);
            } else {
                visibility = View.VISIBLE;
                holder.price.setText(skuUi.getPriceAmount());
                holder.currency.setText(skuUi.getPriceCurrency());
                holder.done.setVisibility(View.GONE);
            }

            holder.price.setVisibility(visibility);
            holder.currency.setVisibility(visibility);
        }
    }

    private static class SkuUi {

        @NonNull
        private static final String TAG = "SkuUi";

        private static final long MICRO = 1_000_000; // defines how much 'micro' is

        @NonNull
        public final Sku sku;

        private final boolean isPurchased;

        @Nullable
        private String description;

        public SkuUi(@NonNull Sku sku, boolean isPurchased) {
            this.sku = sku;
            this.isPurchased = isPurchased;
        }

        @NonNull
        private static String createDescription(@NonNull Sku sku) {
//            String prefix = "donation_";
//            if (sku.id.startsWith(prefix)) {
//                int[] data = new int[]{
//                        1, R.string.donation_1,
//                        4, R.string.donation_4,
//                        10, R.string.donation_10,
//                        20, R.string.donation_20,
//                        50, R.string.donation_50,
//                        99, R.string.donation_99,
//                };
//
//                int price = Integer.parseInt(sku.id.substring(prefix.length()));
//                for (int i = 0; i < data.length; i += 2) {
//                    if (price == data[i]) {
//                        Context context = AppHeap.getContext();
//                        return context.getString(data[i + 1]);
//                    }
//                }
//            }
//
//            Log.wtf(TAG, "Alien sku found!");
//            return "Alien sku found!";
            return sku.description;
        }

        /**
         * @return the price of the sku in {@link #getPriceCurrency() currency}.
         * @see #getPriceCurrency()
         * @see #getDescription()
         */
        @NonNull
        public String getPriceAmount() {
            long amountMicro = sku.detailedPrice.amount;
            if (amountMicro % MICRO == 0) {
                // Format it 'as int' number to
                // get rid of unused comma.
                long amount = amountMicro / MICRO;
                return String.valueOf(amount);
            }

            double amount = (double) amountMicro / MICRO;
            return String.valueOf(amount);
        }

        /**
         * @return the currency of the price.
         * @see #getPriceAmount()
         */
        @NonNull
        public String getPriceCurrency() {
            return sku.detailedPrice.currency;
        }

        /**
         * The thing that you may buy for that money.
         *
         * @see #getPriceAmount()
         */
        @NonNull
        public String getDescription() {
            if (description == null)
                description = createDescription(sku);
            return description;
        }

        /**
         * @return {@code true} if the sku is purchased, {@code false} otherwise.
         */
        public boolean isPurchased() {
            return isPurchased;
        }

    }
}
