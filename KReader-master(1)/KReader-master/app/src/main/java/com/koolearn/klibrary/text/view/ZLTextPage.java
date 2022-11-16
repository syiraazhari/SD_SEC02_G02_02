package com.koolearn.klibrary.text.view;

import com.koolearn.android.util.LogUtil;

import java.util.ArrayList;

final class ZLTextPage {
    final ZLTextWordCursor StartCursor = new ZLTextWordCursor();
    final ZLTextWordCursor EndCursor = new ZLTextWordCursor();
    final ArrayList<ZLTextLineInfo> LineInfos = new ArrayList<ZLTextLineInfo>(); // 一行一行的文字
    int Column0Height;
    int PaintState = PaintStateEnum.NOTHING_TO_PAINT;

    /**
     * 屏幕上东西的坐标
     */
    final ZLTextElementAreaVector TextElementMap = new ZLTextElementAreaVector();

    private int myColumnWidth;
    private int myHeight;
	private boolean myTwoColumnView;

    void setSize(int columnWidth, int height, boolean twoColumnView, boolean keepEndNotStart) {
        if (myColumnWidth == columnWidth && myHeight == height && myColumnWidth == columnWidth) {
            return;
        }
        myColumnWidth = columnWidth;
        myHeight = height;
		myTwoColumnView = twoColumnView;

        if (PaintState != PaintStateEnum.NOTHING_TO_PAINT) {
            LineInfos.clear();
            if (keepEndNotStart) {
                if (!EndCursor.isNull()) {
                    StartCursor.reset();
                    PaintState = PaintStateEnum.END_IS_KNOWN;
                } else if (!StartCursor.isNull()) {
                    EndCursor.reset();
                    PaintState = PaintStateEnum.START_IS_KNOWN;
                }
            } else {
                if (!StartCursor.isNull()) {
                    EndCursor.reset();
                    PaintState = PaintStateEnum.START_IS_KNOWN;
                } else if (!EndCursor.isNull()) {
                    StartCursor.reset();
                    PaintState = PaintStateEnum.END_IS_KNOWN;
                }
            }
        }
    }

    void reset() {
        StartCursor.reset();
        EndCursor.reset();
        LineInfos.clear();
        PaintState = PaintStateEnum.NOTHING_TO_PAINT;
    }

    void moveStartCursor(ZLTextParagraphCursor cursor) {
        LogUtil.i13("cursor:" + cursor); // ZLTextParagraphCursor [0 (0..5)]] myElements.size() = 5
        StartCursor.setCursor(cursor);
        EndCursor.reset();
        LineInfos.clear();
        PaintState = PaintStateEnum.START_IS_KNOWN;
    }

    void moveStartCursor(int paragraphIndex, int wordIndex, int charIndex) { // 章节
        if (StartCursor.isNull()) {
            StartCursor.setCursor(EndCursor);
        }
        StartCursor.moveToParagraph(paragraphIndex);
        StartCursor.moveTo(wordIndex, charIndex);
        EndCursor.reset();
        LineInfos.clear();
        PaintState = PaintStateEnum.START_IS_KNOWN;
    }
//
//    void moveStartCursor(float percent) { // 章节
//        if (StartCursor.isNull()) {
//            StartCursor.setCursor(EndCursor);
//        }
//        StartCursor.moveToParagraph(paragraphIndex);
//        StartCursor.moveTo(wordIndex, charIndex);
//        EndCursor.reset();
//        LineInfos.clear();
//        PaintState = PaintStateEnum.START_IS_KNOWN;
//    }

    void moveEndCursor(int paragraphIndex, int wordIndex, int charIndex) {

        if (EndCursor.isNull()) {
            EndCursor.setCursor(StartCursor);
        }
        EndCursor.moveToParagraph(paragraphIndex);
        if ((paragraphIndex > 0) && (wordIndex == 0) && (charIndex == 0)) {
            EndCursor.previousParagraph();
            EndCursor.moveToParagraphEnd();
        } else {
            EndCursor.moveTo(wordIndex, charIndex);
        }
        StartCursor.reset();
        LineInfos.clear();
        PaintState = PaintStateEnum.END_IS_KNOWN;
    }

    int getTextWidth() {
        return myColumnWidth;
    }

    int getTextHeight() {
        return myHeight;
    }

	boolean twoColumnView() {
		return myTwoColumnView;
	}

    boolean isEmptyPage() {
        for (ZLTextLineInfo info : LineInfos) {
            if (info.IsVisible) {
                return false;
            }
        }
        return true;
    }

    void findLineFromStart(ZLTextWordCursor cursor, int overlappingValue) {
        if (LineInfos.isEmpty() || (overlappingValue == 0)) {
            cursor.reset();
            return;
        }
        ZLTextLineInfo info = null;
        for (ZLTextLineInfo i : LineInfos) {
            info = i;
            if (info.IsVisible) {
                --overlappingValue;
                if (overlappingValue == 0) {
                    break;
                }
            }
        }
        cursor.setCursor(info.ParagraphCursor);
        cursor.moveTo(info.EndElementIndex, info.EndCharIndex);
    }

    void findLineFromEnd(ZLTextWordCursor cursor, int overlappingValue) {
        if (LineInfos.isEmpty() || (overlappingValue == 0)) {
            cursor.reset();
            return;
        }
        final ArrayList<ZLTextLineInfo> infos = LineInfos;
        final int size = infos.size();
        ZLTextLineInfo info = null;
        for (int i = size - 1; i >= 0; --i) {
            info = infos.get(i);
            if (info.IsVisible) {
                --overlappingValue;
                if (overlappingValue == 0) {
                    break;
                }
            }
        }
        cursor.setCursor(info.ParagraphCursor);
        cursor.moveTo(info.StartElementIndex, info.StartCharIndex);
    }

    void findPercentFromStart(ZLTextWordCursor cursor, int percent) {
        if (LineInfos.isEmpty()) {
            cursor.reset();
            return;
        }
        int height = myHeight * percent / 100;
        boolean visibleLineOccured = false;
        ZLTextLineInfo info = null;
        for (ZLTextLineInfo i : LineInfos) {
            info = i;
            if (info.IsVisible) {
                visibleLineOccured = true;
            }
            height -= info.Height + info.Descent + info.VSpaceAfter;
            if (visibleLineOccured && (height <= 0)) {
                break;
            }
        }
        cursor.setCursor(info.ParagraphCursor);
        cursor.moveTo(info.EndElementIndex, info.EndCharIndex);
    }
}
