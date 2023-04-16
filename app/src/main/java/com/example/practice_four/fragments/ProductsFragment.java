package com.example.practice_four.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice_four.R;
import com.example.practice_four.model.Product;
import com.example.practice_four.model.ProductsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//import edu.psu.sweng888.lessonfive_fragmentsui.R;
//import edu.psu.sweng888.lessonfive_fragmentsui.data.Book;
//import edu.psu.sweng888.lessonfive_fragmentsui.adapter.BookAdapter;
//import edu.psu.sweng888.lessonfive_fragmentsui.data.BookDatabaseHelper;

public class ProductsFragment extends Fragment {

    private ProductsAdapter productAdapter;
    private RecyclerView mRecyclerView;

    private DatabaseReference firebaseDatabase;

    private ProductsAdapter mProductsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /** Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        /** Instantiate the RecyclerView */
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        mAddToCartButton = container.findViewById(R.id.add_to_cart_text_view);

        /** Implement the Call to FirebaseProductDAO */

        firebaseDatabase = FirebaseDatabase.getInstance().getReference("products");

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> productList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    // Set key of product so it can be easily edited/deleted later
                    product.setKey(dataSnapshot.getKey());
                    productList.add(product);
                }
                mProductsAdapter = new ProductsAdapter(productList);
                mRecyclerView.setAdapter(mProductsAdapter);
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
