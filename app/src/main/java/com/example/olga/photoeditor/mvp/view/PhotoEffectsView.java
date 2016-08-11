package com.example.olga.photoeditor.mvp.view;

import android.graphics.Bitmap;
import android.media.effect.Effect;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.olga.photoeditor.db.EffectDataSource;

/**
 * Date: 03.08.16
 * Time: 19:39
 *
 * @author Olga
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface PhotoEffectsView extends MvpView {

    void showPhoto();

    void savePhoto();

    void setTextures(int textures[]);

    void changeTexture(int number, int data);

    @StateStrategyType(value = SingleStateStrategy.class)
    void setBitmap(Bitmap bitmap);

    void setResultTexture(int resultTexture);

    void setEffect();

    void setEffectsData(EffectDataSource data);

    void applyEffect(int sourceTexture, int destinationTexture, Effect effect, int height, int width);

}
