package com.example.olga.photoeditor.ui;

import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;

import com.example.olga.photoeditor.models.Filter;
import com.example.olga.photoeditor.models.Property;

import java.util.List;

/**
 * Date: 26.07.16
 * Time: 19:21
 *
 * @author Olga
 */
public class EffectsInitializer {

    public static Effect[] initEffects(List<Property> changedProperties, EffectContext effectContext) {
        Effect effects[] = new Effect[changedProperties.size()];
        EffectFactory effectFactory = effectContext.getFactory();

        for (int i = 0; i < changedProperties.size(); i++) {
            Property property = Property.valueOf(changedProperties.get(i).getPropertyName());

            float value = changedProperties.get(i).getCurrentValue();
            if (effects[i] != null) {
                effects[i].release();
            }
            switch (property) {
                //Standard Properties
                case BRIGHTNESS:
                    effects[i] = effectFactory.createEffect(EffectFactory.EFFECT_BRIGHTNESS);
                    effects[i].setParameter("brightness", value);
                    break;

                case CONTRAST:
                    effects[i] = effectFactory.createEffect(EffectFactory.EFFECT_CONTRAST);
                    effects[i].setParameter("contrast", value);
                    break;

                case SATURATE:
                    effects[i] = effectFactory.createEffect(EffectFactory.EFFECT_SATURATE);
                    effects[i].setParameter("scale", value);
                    break;

                case SHARPEN:
                    effects[i] = effectFactory.createEffect(EffectFactory.EFFECT_SHARPEN);
                    effects[i].setParameter("scale", value);
                    break;

                //Extend Properties
                case AUTOFIX:
                    effects[i] = effectFactory.createEffect(EffectFactory.EFFECT_AUTOFIX);
                    effects[i].setParameter("scale", value);
                    break;

                case BLACKWHITE:
                    effects[i] = effectFactory.createEffect(EffectFactory.EFFECT_BLACKWHITE);
                    if (value >= 0.5) {
                        effects[i].setParameter("black", value);
                    } else {
                        effects[i].setParameter("white", (1.0f - value));
                    }
                    break;

                case FILLIGHT:
                    effects[i] = effectFactory.createEffect(EffectFactory.EFFECT_FILLLIGHT);
                    effects[i].setParameter("strength", value);
                    break;

                case GRAIN:
                    effects[i] = effectFactory.createEffect(EffectFactory.EFFECT_GRAIN);
                    effects[i].setParameter("strength", value);
                    break;

                case TEMPERATURE:
                    effects[i] = effectFactory.createEffect(EffectFactory.EFFECT_TEMPERATURE);
                    effects[i].setParameter("scale", value);
                    break;

                case FISHEYE:
                    effects[i] = effectFactory.createEffect(EffectFactory.EFFECT_FISHEYE);
                    effects[i].setParameter("scale", value);
                    break;

                case VIGNETTE:
                    effects[i] = effectFactory.createEffect(EffectFactory.EFFECT_VIGNETTE);
                    effects[i].setParameter("scale", value);
                    break;

                default:
                    break;
            }
        }
        return effects;
    }

    public static Effect initFilters(String currentFilter, EffectContext effectContext) {
        Effect effect;
        EffectFactory effectFactory = effectContext.getFactory();

        Filter filter = Filter.valueOf(currentFilter);

        switch (filter) {

            case FLIPVERT:
                effect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FLIP);
                effect.setParameter("vertical", true);
                break;

            case FLIPHOR:
                effect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FLIP);
                effect.setParameter("horizontal", true);
                break;

            case CROSSPROCESS:
                effect = effectFactory.createEffect(
                        EffectFactory.EFFECT_CROSSPROCESS);
                break;

            case DOCUMENTARY:
                effect = effectFactory.createEffect(
                        EffectFactory.EFFECT_DOCUMENTARY);
                break;

            case GRAYSCALE:
                effect = effectFactory.createEffect(
                        EffectFactory.EFFECT_GRAYSCALE);
                break;

            case LOMOISH:
                effect = effectFactory.createEffect(
                        EffectFactory.EFFECT_LOMOISH);
                break;

            case NEGATIVE:
                effect = effectFactory.createEffect(
                        EffectFactory.EFFECT_NEGATIVE);
                break;

            case POSTERIZE:
                effect = effectFactory.createEffect(
                        EffectFactory.EFFECT_POSTERIZE);
                break;

            case SEPIA:
                effect = effectFactory.createEffect(
                        EffectFactory.EFFECT_SEPIA);
                break;

            default:
                effect = null;
                break;
        }

        return effect;

    }
}
