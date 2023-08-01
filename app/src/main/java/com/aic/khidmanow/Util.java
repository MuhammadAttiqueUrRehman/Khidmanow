package com.aic.khidmanow;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.View;

import layout.utils.LocalManager;

public class Util {

    public void setUpStepsBar(View v, int total_pages, int stepNo) {
//        int total_pages = intromanager.getPageCount();
        int[] step_view_id = {R.id.iv_step_01, R.id.iv_step_02, R.id.iv_step_03, R.id.iv_step_04,
                R.id.iv_step_05, R.id.iv_step_06, R.id.iv_step_07, R.id.iv_step_08};
        for (int i = 0; i < total_pages; i++) {
            v.findViewById(step_view_id[i]).setVisibility(View.VISIBLE);
        }
        if (total_pages != stepNo)
            for (int i = 0; i <= stepNo; i++) {
                ((AppCompatImageView) v.findViewById(step_view_id[i])).setImageResource(R.drawable.ic_step_done);
            }
    }

    public String processJsonForLanguage(String json, Context context) {
        String lang = LocalManager.getLanguagePref(context);
        if (lang.equals(LocalManager.ARABIC))
            return json.replace("_a\"", "\"");
        else return json;
    }
}
