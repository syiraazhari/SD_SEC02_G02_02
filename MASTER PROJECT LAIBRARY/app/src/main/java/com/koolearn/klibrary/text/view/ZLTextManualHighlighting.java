package com.koolearn.klibrary.text.view;

import com.koolearn.klibrary.core.util.ZLColor;

class ZLTextManualHighlighting extends ZLTextSimpleHighlighting {
	ZLTextManualHighlighting(ZLTextView view, ZLTextPosition start, ZLTextPosition end) {
		super(view, start, end);
	}

	@Override
	public ZLColor getBackgroundColor() {
		return View.getHighlightingBackgroundColor();
	}

	@Override
	public ZLColor getForegroundColor() {
		return View.getHighlightingForegroundColor();
	}

	@Override
	public ZLColor getOutlineColor() {
		return null;
	}
}
