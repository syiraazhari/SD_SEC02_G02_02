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

public final class FragmentBookBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnBooking;

  @NonNull
  public final Button btnHistory;

  @NonNull
  public final ImageView imageView3;

  @NonNull
  public final TextView textView15;

  private FragmentBookBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnBooking,
      @NonNull Button btnHistory, @NonNull ImageView imageView3, @NonNull TextView textView15) {
    this.rootView = rootView;
    this.btnBooking = btnBooking;
    this.btnHistory = btnHistory;
    this.imageView3 = imageView3;
    this.textView15 = textView15;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentBookBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentBookBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_book, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentBookBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnBooking;
      Button btnBooking = ViewBindings.findChildViewById(rootView, id);
      if (btnBooking == null) {
        break missingId;
      }

      id = R.id.btnHistory;
      Button btnHistory = ViewBindings.findChildViewById(rootView, id);
      if (btnHistory == null) {
        break missingId;
      }

      id = R.id.imageView3;
      ImageView imageView3 = ViewBindings.findChildViewById(rootView, id);
      if (imageView3 == null) {
        break missingId;
      }

      id = R.id.textView15;
      TextView textView15 = ViewBindings.findChildViewById(rootView, id);
      if (textView15 == null) {
        break missingId;
      }

      return new FragmentBookBinding((ConstraintLayout) rootView, btnBooking, btnHistory,
          imageView3, textView15);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
