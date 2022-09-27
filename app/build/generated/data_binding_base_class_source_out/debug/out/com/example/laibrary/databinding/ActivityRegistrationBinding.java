// Generated by view binder compiler. Do not edit!
package com.example.laibrary.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.laibrary.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRegistrationBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnRegister;

  @NonNull
  public final EditText etAge;

  @NonNull
  public final EditText etUserEmail;

  @NonNull
  public final EditText etUserName;

  @NonNull
  public final EditText etUserPassword;

  @NonNull
  public final ImageView ivProfile;

  @NonNull
  public final TextView textView15;

  @NonNull
  public final TextView tvUserLogin;

  private ActivityRegistrationBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button btnRegister, @NonNull EditText etAge, @NonNull EditText etUserEmail,
      @NonNull EditText etUserName, @NonNull EditText etUserPassword, @NonNull ImageView ivProfile,
      @NonNull TextView textView15, @NonNull TextView tvUserLogin) {
    this.rootView = rootView;
    this.btnRegister = btnRegister;
    this.etAge = etAge;
    this.etUserEmail = etUserEmail;
    this.etUserName = etUserName;
    this.etUserPassword = etUserPassword;
    this.ivProfile = ivProfile;
    this.textView15 = textView15;
    this.tvUserLogin = tvUserLogin;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRegistrationBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRegistrationBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_registration, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRegistrationBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnRegister;
      Button btnRegister = ViewBindings.findChildViewById(rootView, id);
      if (btnRegister == null) {
        break missingId;
      }

      id = R.id.etAge;
      EditText etAge = ViewBindings.findChildViewById(rootView, id);
      if (etAge == null) {
        break missingId;
      }

      id = R.id.etUserEmail;
      EditText etUserEmail = ViewBindings.findChildViewById(rootView, id);
      if (etUserEmail == null) {
        break missingId;
      }

      id = R.id.etUserName;
      EditText etUserName = ViewBindings.findChildViewById(rootView, id);
      if (etUserName == null) {
        break missingId;
      }

      id = R.id.etUserPassword;
      EditText etUserPassword = ViewBindings.findChildViewById(rootView, id);
      if (etUserPassword == null) {
        break missingId;
      }

      id = R.id.ivProfile;
      ImageView ivProfile = ViewBindings.findChildViewById(rootView, id);
      if (ivProfile == null) {
        break missingId;
      }

      id = R.id.textView15;
      TextView textView15 = ViewBindings.findChildViewById(rootView, id);
      if (textView15 == null) {
        break missingId;
      }

      id = R.id.tvUserLogin;
      TextView tvUserLogin = ViewBindings.findChildViewById(rootView, id);
      if (tvUserLogin == null) {
        break missingId;
      }

      return new ActivityRegistrationBinding((ConstraintLayout) rootView, btnRegister, etAge,
          etUserEmail, etUserName, etUserPassword, ivProfile, textView15, tvUserLogin);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
