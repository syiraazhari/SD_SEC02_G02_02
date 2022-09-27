// Generated by view binder compiler. Do not edit!
package com.example.laibrary.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public final class ActivityBookingBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnBook;

  @NonNull
  public final Button btnSetDate;

  @NonNull
  public final EditText etFullName;

  @NonNull
  public final EditText etIc;

  @NonNull
  public final EditText etPnumber;

  @NonNull
  public final EditText etQuantity;

  @NonNull
  public final Spinner namaBuku;

  @NonNull
  public final TextView textView5;

  @NonNull
  public final TextView textView6;

  @NonNull
  public final TextView tvRentDate;

  private ActivityBookingBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnBook,
      @NonNull Button btnSetDate, @NonNull EditText etFullName, @NonNull EditText etIc,
      @NonNull EditText etPnumber, @NonNull EditText etQuantity, @NonNull Spinner namaBuku,
      @NonNull TextView textView5, @NonNull TextView textView6, @NonNull TextView tvRentDate) {
    this.rootView = rootView;
    this.btnBook = btnBook;
    this.btnSetDate = btnSetDate;
    this.etFullName = etFullName;
    this.etIc = etIc;
    this.etPnumber = etPnumber;
    this.etQuantity = etQuantity;
    this.namaBuku = namaBuku;
    this.textView5 = textView5;
    this.textView6 = textView6;
    this.tvRentDate = tvRentDate;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityBookingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityBookingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_booking, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityBookingBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnBook;
      Button btnBook = ViewBindings.findChildViewById(rootView, id);
      if (btnBook == null) {
        break missingId;
      }

      id = R.id.btnSetDate;
      Button btnSetDate = ViewBindings.findChildViewById(rootView, id);
      if (btnSetDate == null) {
        break missingId;
      }

      id = R.id.etFullName;
      EditText etFullName = ViewBindings.findChildViewById(rootView, id);
      if (etFullName == null) {
        break missingId;
      }

      id = R.id.etIc;
      EditText etIc = ViewBindings.findChildViewById(rootView, id);
      if (etIc == null) {
        break missingId;
      }

      id = R.id.etPnumber;
      EditText etPnumber = ViewBindings.findChildViewById(rootView, id);
      if (etPnumber == null) {
        break missingId;
      }

      id = R.id.etQuantity;
      EditText etQuantity = ViewBindings.findChildViewById(rootView, id);
      if (etQuantity == null) {
        break missingId;
      }

      id = R.id.namaBuku;
      Spinner namaBuku = ViewBindings.findChildViewById(rootView, id);
      if (namaBuku == null) {
        break missingId;
      }

      id = R.id.textView5;
      TextView textView5 = ViewBindings.findChildViewById(rootView, id);
      if (textView5 == null) {
        break missingId;
      }

      id = R.id.textView6;
      TextView textView6 = ViewBindings.findChildViewById(rootView, id);
      if (textView6 == null) {
        break missingId;
      }

      id = R.id.tvRentDate;
      TextView tvRentDate = ViewBindings.findChildViewById(rootView, id);
      if (tvRentDate == null) {
        break missingId;
      }

      return new ActivityBookingBinding((ConstraintLayout) rootView, btnBook, btnSetDate,
          etFullName, etIc, etPnumber, etQuantity, namaBuku, textView5, textView6, tvRentDate);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}