// Generated by view binder compiler. Do not edit!
package com.example.laibrary.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public final class FragmentHomeBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnAboutUs;

  @NonNull
  public final Button btnBookingroom;

  @NonNull
  public final Button btnFeedback;

  @NonNull
  public final Button btnListOfBook;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final TextView textView15;

  private FragmentHomeBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnAboutUs,
      @NonNull Button btnBookingroom, @NonNull Button btnFeedback, @NonNull Button btnListOfBook,
      @NonNull ImageView imageView, @NonNull TextView textView15) {
    this.rootView = rootView;
    this.btnAboutUs = btnAboutUs;
    this.btnBookingroom = btnBookingroom;
    this.btnFeedback = btnFeedback;
    this.btnListOfBook = btnListOfBook;
    this.imageView = imageView;
    this.textView15 = textView15;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentHomeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentHomeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_home, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentHomeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnAboutUs;
      Button btnAboutUs = ViewBindings.findChildViewById(rootView, id);
      if (btnAboutUs == null) {
        break missingId;
      }

      id = R.id.btnBookingroom;
      Button btnBookingroom = ViewBindings.findChildViewById(rootView, id);
      if (btnBookingroom == null) {
        break missingId;
      }

      id = R.id.btnFeedback;
      Button btnFeedback = ViewBindings.findChildViewById(rootView, id);
      if (btnFeedback == null) {
        break missingId;
      }

      id = R.id.btnListOfBook;
      Button btnListOfBook = ViewBindings.findChildViewById(rootView, id);
      if (btnListOfBook == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.textView15;
      TextView textView15 = ViewBindings.findChildViewById(rootView, id);
      if (textView15 == null) {
        break missingId;
      }

      return new FragmentHomeBinding((ConstraintLayout) rootView, btnAboutUs, btnBookingroom,
          btnFeedback, btnListOfBook, imageView, textView15);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}