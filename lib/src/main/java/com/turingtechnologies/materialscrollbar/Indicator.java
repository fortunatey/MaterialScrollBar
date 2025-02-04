/*
 * Copyright © 2015, Turing Technologies, an unincorporated organisation of Wynne Plaga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.turingtechnologies.materialscrollbar;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;

public abstract class Indicator extends RelativeLayout{

    protected TextView textView;
    private Context context;

    public Indicator(Context context) {
        super(context);
        this.context = context;
    }

    void linkToScrollBar(MaterialScrollBar materialScrollBar){
        if(Build.VERSION.SDK_INT >= 16){
            setBackground(ContextCompat.getDrawable(context, R.drawable.indicator));
        } else {
            setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.indicator));
        }
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(Utils.getDP(getIndicatorWidth(), this), Utils.getDP(getIndicatorHeight(), this));
        lp.setMargins(0, 0, Utils.getDP(8, this), 0);
        setVisibility(INVISIBLE);

        textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getTextSize());
        RelativeLayout.LayoutParams tvlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvlp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        addView(textView, tvlp);

        ((GradientDrawable)getBackground()).setColor(materialScrollBar.handleColour);

        lp.addRule(ALIGN_RIGHT, materialScrollBar.getId());
        ((ViewGroup)materialScrollBar.getParent()).addView(this, lp);
    }

    /**
     * Used by the materialScrollBar to move the indicator with the handle
     * @param y Position to which the indicator should move.
     */
    void setScroll(float y){
        //Displace the indicator upward so that the carrot extends from the centre of the handle.
        y += Utils.getDP(24 - getIndicatorHeight(), this);
        //If the indicator is hidden by the top of the screen, it is inverted and displaced downward.
        if(y < 0){
            y += Utils.getDP(getIndicatorHeight(), this);
            ViewHelper.setScaleY(this, -1F);
            ViewHelper.setScaleY(textView, -1F);
            ViewHelper.setY(this, y);
        } else {
            ViewHelper.setScaleY(this, 1F);
            ViewHelper.setScaleY(textView, 1F);
            ViewHelper.setY(this, y);
        }
    }

    /**
     * Used by the materialScrollBar to change the text colour for the indicator.
     * @param colour The desired text colour.
     */
    void setTextColour(int colour){
        textView.setTextColor(colour);
    }

    abstract String getTextElement(Integer currentSection, RecyclerView.Adapter adapter);

    abstract int getIndicatorHeight();

    abstract int getIndicatorWidth();

    abstract void testAdapter(RecyclerView.Adapter adapter);

    abstract int getTextSize();

}
