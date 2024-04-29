package com.example.netstore.viewModels;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.netstore.config.ObserverObject;
import com.example.netstore.config.SharedPreferencesConfig;
import com.example.netstore.models.Client;
import com.example.netstore.models.Employee;
import com.example.netstore.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class UserViewModel {
    private MutableLiveData<ObserverObject> mutableLiveData = new MutableLiveData<>();
    public LiveData<ObserverObject> getInfoData() {
        return mutableLiveData;
    }

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    public UserViewModel() {
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void checkLogin(String login) {
        // TODO
    }

    public void authenticationUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String uid = firebaseAuth.getCurrentUser().getUid();

                FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                CollectionReference collectionReference = fStore.collection("users");

                collectionReference.whereEqualTo("firebaseId", uid).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot documentSnap : queryDocumentSnapshots.getDocuments()) {
                                    User authUser = documentSnap.toObject(User.class);
                                    mutableLiveData.postValue(new ObserverObject("auth user", true, authUser));
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mutableLiveData.postValue(new ObserverObject("auth user", false));
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mutableLiveData.postValue(new ObserverObject("auth user", false));
            }
        });
    }

    public void registrationUser(String email, String password, User user) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = firebaseAuth.getCurrentUser().getUid();
                    user.firebaseId = uid;

                    DocumentReference docRef = firestore.collection("users").document();

                    user._id = docRef.getId();

                    docRef.set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    mutableLiveData.postValue(new ObserverObject("reg user", true, user));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mutableLiveData.postValue(new ObserverObject("reg user", false));
                                }
                            });
                })
                .addOnFailureListener(e -> {
                    mutableLiveData.postValue(new ObserverObject("reg user", false));
                });
    }

    public void updateUser(User user) {
        DocumentReference docRef = firestore.collection("users").document(user._id);

        docRef.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        mutableLiveData.postValue(new ObserverObject("update user", true));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("update user", false));
                    }
                });
    }

    public void getClients() {
        firestore.collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Client> clientList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                            Client client = new Client();

                            switch (documentSnapshot.getString("type")) {
                                case "Employee":
                                    client.type = User.UserType.Employee;
                                    break;
                                case "Client":
                                    client.type = User.UserType.Client;
                                    break;
                            }

                            if (client.type != User.UserType.Client)
                                continue;

                            client._id = documentSnapshot.getString("_id");
                            client.firebaseId = documentSnapshot.getString("firebaseId");
                            client.name = documentSnapshot.getString("name");
                            client.surname = documentSnapshot.getString("surname");
                            client.address = documentSnapshot.getString("address");
                            client.email = documentSnapshot.getString("email");
                            client.birthday = documentSnapshot.getDate("birthday");

                            clientList.add(client);
                        }
                        mutableLiveData.postValue(new ObserverObject("get list client", true, clientList));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("get list client", false));
                    }
                });
    }

    public void getEmployees() {
        firestore.collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Employee> employeeList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                            Employee employee = new Employee();

                            switch (documentSnapshot.getString("type")) {
                                case "Employee":
                                    employee.type = User.UserType.Employee;
                                    break;
                                case "Client":
                                    employee.type = User.UserType.Client;
                                    break;
                            }

                            if (employee.type != User.UserType.Employee)
                                continue;

                            employee._id = documentSnapshot.getString("_id");
                            employee.firebaseId = documentSnapshot.getString("firebaseId");
                            employee.name = documentSnapshot.getString("name");
                            employee.surname = documentSnapshot.getString("surname");
                            employee.email = documentSnapshot.getString("email");
                            employee.birthday = documentSnapshot.getDate("birthday");

                            employee.department = documentSnapshot.getString("department");
                            employee.hireDate = documentSnapshot.getDate("hireDate");
                            employee.job = documentSnapshot.getString("job");
                            employee.salary = documentSnapshot.get("salary", double.class);

                            employeeList.add(employee);
                        }
                        mutableLiveData.postValue(new ObserverObject("get list employee", true, employeeList));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("get list employee", false));
                    }
                });
    }

    public void getEmployee(User user) {
        if (user.type != User.UserType.Employee) {
            mutableLiveData.postValue(new ObserverObject("get employee", false));
            return;
        }

        DocumentReference docRef = firestore.collection("users").document(user._id);

        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Employee employee = documentSnapshot.toObject(Employee.class);
                            mutableLiveData.postValue(new ObserverObject("get employee", true, employee));
                        } else {
                            mutableLiveData.postValue(new ObserverObject("get employee", false));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("get employee", false));
                    }
                });
    }

    public void getClient(User user) {
        if (user.type != User.UserType.Client) {
            mutableLiveData.postValue(new ObserverObject("get client", false));
            return;
        }

        DocumentReference docRef = firestore.collection("users").document(user._id);

        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Client client = documentSnapshot.toObject(Client.class);
                            mutableLiveData.postValue(new ObserverObject("get client", true, client));
                        } else {
                            mutableLiveData.postValue(new ObserverObject("get client", false));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mutableLiveData.postValue(new ObserverObject("get client", false));
                    }
                });
    }

    public void saveCurrentUser(Context context, User user) {
        String currentUserJson = new Gson().toJson(user, User.class);
        SharedPreferences.Editor editorSP = context.getSharedPreferences(SharedPreferencesConfig.SP_FILE_TAG, Context.MODE_PRIVATE).edit();
        editorSP.putString(SharedPreferencesConfig.SP_USER_TAG, currentUserJson);
        editorSP.apply();
    }
}
