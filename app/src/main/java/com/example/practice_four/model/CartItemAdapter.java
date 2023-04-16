package com.example.practice_four.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice_four.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private List<CartItem> mCartItems;

    private FirebaseAuth mFirebaseAuth;

    private DatabaseReference cartDbRef;
    public CartItemAdapter(List<CartItem> cartItems) {
        mCartItems = cartItems;
    }

    private Context mContext;

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mContext = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        CardView cardView = (CardView) view.findViewById(R.id.cart_card_view);

        // Get DB ref for the user's cart
        String uid = mFirebaseAuth.getInstance().getCurrentUser().getUid();
        cartDbRef = FirebaseDatabase.getInstance().getReference().child("carts").child(uid);

        cardView.setUseCompatPadding(true); // Optional: adds padding for pre-lollipop devices
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem item = mCartItems.get(position);
        holder.mRemoveFromCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveItemFromCart(item);
            }
        });
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mCartItems.size();
    }

    public void setCartItems(List<CartItem> cartItems) {
        mCartItems = cartItems;
        notifyDataSetChanged();
    }

    static class CartItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        private TextView mSellerTextView;
        private TextView mDescriptionDateTextView;
        private TextView mPriceTextView;
        private TextView mQuantityTextView;
        private Button mRemoveFromCartButton;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.cart_name_text_view);
            mSellerTextView = itemView.findViewById(R.id.cart_seller_text_view);
            mDescriptionDateTextView = itemView.findViewById(R.id.cart_description_text_view);
            mPriceTextView = itemView.findViewById(R.id.cart_price_text_view);
            mQuantityTextView = itemView.findViewById(R.id.cart_quantity_text_view);
            mRemoveFromCartButton = itemView.findViewById(R.id.remove_from_cart_button);
        }

        public void bind(CartItem item) {
            mNameTextView.setText(item.getName());
            mSellerTextView.setText("Seller: "+item.getSeller());
            mDescriptionDateTextView.setText("Description: "+item.getDescription());
            mPriceTextView.setText("Price: "+item.getPrice().toString());
            mQuantityTextView.setText("Quantity: "+item.getQuantity());
            mRemoveFromCartButton.setText("Remove Item");
        }
    }

    public void RemoveItemFromCart(CartItem cartItem) {
        // Remove item from user's cart. If there is more than 1 of this item, then just decrement quantity.

        String productKey = cartItem.getKey();
        Log.d("firebase", "prod key: " + productKey);
        DatabaseReference prodCartDbRef = cartDbRef.child(productKey);

        prodCartDbRef.child("quantity").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    return;
                }

                int quantity = task.getResult().getValue(Integer.class);

                //Remove item completely if only 1 in cart
                if (quantity == 1) {
                    prodCartDbRef.removeValue();
                }
                else {
                    // Decrement quantity
                    CartItem item = new CartItem(quantity - 1, cartItem.getName(), cartItem.getSeller(), cartItem.getDescription(), cartItem.getPrice());
                    prodCartDbRef.setValue(item);
                }
                Toast.makeText(mContext, "Item Removed from Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

