package com.ui.AdminStaff;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.view.Change;
import com.koolearn.klibrary.ui.android.R;

public class ViewVoucher extends AppCompatActivity {

    private EditText viewVoucherCode, viewVoucherDis, viewVoucherNum;
    private Button btnCancel, btnConfirm, btnDelete;
    private String code, dis, num, id;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_voucher);

        viewVoucherCode = findViewById(R.id.et_viewVoucherName);
        viewVoucherDis = findViewById(R.id.et_viewVoucherDis);
        viewVoucherNum = findViewById(R.id.et_viewVoucherNum);
        btnCancel = findViewById(R.id.btn_viewvouchercancel);
        btnConfirm = findViewById(R.id.btn_viewvoucherconfirm);
        btnDelete = findViewById(R.id.btn_viewvoucherdelete);

        code = getIntent().getStringExtra("keyvouchercode");
        dis = getIntent().getStringExtra("keyvoucherdis");
        num = getIntent().getStringExtra("keyvouchernum");
        id = getIntent().getStringExtra("keyvoucherid");
        Log.i(TAG,"idvoucher is" +id);

        viewVoucherCode.setText(code);
        viewVoucherDis.setText(dis);
        viewVoucherNum.setText(num);

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("voucher").child(code);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newcode, newdis, newnum;
                newcode = viewVoucherCode.getText().toString();
                newdis = viewVoucherDis.getText().toString();
                newnum = viewVoucherNum.getText().toString();

                if(!newcode.isEmpty() && !newdis.isEmpty() && !newnum.isEmpty()){
                    Double disdouble = Double.parseDouble(newdis);
                    Integer numint = Integer.parseInt((newnum));
                    if ( disdouble>0 && disdouble<=100){
                        if(numint>0){

                            Voucher voucher = new Voucher(newcode, newdis, newnum, id);
                            databaseReference.setValue(voucher);
                            startActivity(new Intent(ViewVoucher.this, ChangeVoucher.class));

                        }
                        else {
                            Toast.makeText(ViewVoucher.this, "Please Enter amount more than 0", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(ViewVoucher.this, "Please Enter discount between 0 and 100", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(ViewVoucher.this, "Please Enter all information", Toast.LENGTH_SHORT).show();
                }





            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ViewVoucher.this);
                dialog.setTitle("Are You Sure?");
                dialog.setMessage("Deleting this voucher will result in completely removing this voucher from the system");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ViewVoucher.this, "Account deleted", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(ViewVoucher.this, ChangeVoucher.class));
                                }else{
                                    Toast.makeText(ViewVoucher.this, "cannot delete", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(ViewVoucher.this, task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

    }
}