package com.example.practice_four.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice_four.R;
import com.example.practice_four.model.CartItem;
import com.example.practice_four.model.CartItemAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TextView emptyCartTextView;
    private DatabaseReference userCartDbRef;
    private FirebaseAuth mFirebaseAuth;
    private CartItemAdapter mCartItemAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /** Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        emptyCartTextView = view.findViewById(R.id.empty_cart_text_View);
        /** Instantiate the RecyclerView */
        mRecyclerView = view.findViewById(R.id.cart_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /** Implement the Call to FirebaseProductDAO */

        String uid = mFirebaseAuth.getInstance().getCurrentUser().getUid();

        //Get the cart for this user
        userCartDbRef = FirebaseDatabase.getInstance().getReference("carts").child(uid);

        userCartDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<CartItem> cartItems = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartItem cartItem = dataSnapshot.getValue(CartItem.class);
                    // Set key for cart item, which is the equal to the product key it represents
                    cartItem.setKey(dataSnapshot.getKey());
                    cartItems.add(cartItem);
                }
                mCartItemAdapter = new CartItemAdapter(cartItems);
                mRecyclerView.setAdapter(mCartItemAdapter);

                // If the cart is empty show emptyCartTextView
                if (cartItems.isEmpty()) {
                    emptyCartTextView.setVisibility(View.VISIBLE);
                }
                else {
                    emptyCartTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProductsFragment", "Error retrieving products from database", error.toException());
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
