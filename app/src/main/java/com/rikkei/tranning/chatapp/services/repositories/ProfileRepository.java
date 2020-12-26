package com.rikkei.tranning.chatapp.services.repositories;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rikkei.tranning.chatapp.services.models.UserModel;

public class ProfileRepository {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference;
    private UserModel account = new UserModel();

    public void infoUserFromFirebase(final DataStatus dataStatus) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
        final String userId = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                account = dataSnapshot.getValue(UserModel.class);
                dataStatus.DataIsLoaded(account);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateInforFromFirebase(String key, String value) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
        String userId = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        databaseReference.child(userId).child(key).setValue(value);
    }

    public interface DataStatus {
        void DataIsLoaded(UserModel user);
    }
}
