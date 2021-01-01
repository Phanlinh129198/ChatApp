package com.linh.doan.services.repositories;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.linh.doan.services.models.UserModel;

public class SignUpRepository {
    DatabaseReference databaseReference;

    public void createUserFromFirebase(UserModel user, String userId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        databaseReference.child(userId).setValue(user);
    }
}
