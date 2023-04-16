package com.example.practice_four.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

public class ProductsActivity extends AppCompatActivity {

    private TextView mTextViewEmail;
    private RecyclerView mRecyclerView;
    private DatabaseReference firebaseDatabase;
    private ProductsAdapter mProductsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        mTextViewEmail = findViewById(R.id.text_view_email);
        String email = "Email: "+getIntent().getStringExtra("email");
        mTextViewEmail.setText(email);

        mRecyclerView = findViewById(R.id.product_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        /** Implement the Call to FirebaseProductDAO */

        firebaseDatabase = FirebaseDatabase.getInstance().getReference("products");

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> productList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    product.setKey(dataSnapshot.getKey());
                    productList.add(product);
                }
                mProductsAdapter = new ProductsAdapter(productList);
                mRecyclerView.setAdapter(mProductsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProductListActivity", "Error retrieving products from database", error.toException());
            }
        });
    }
}