package com.github.axet.lookup.common;

import java.util.ArrayList;
import java.util.List;

public class FeatureSet extends ArrayList<Feature> {
    private static final long serialVersionUID = -9077324933940287714L;

    public List<FeatureK> k(IntegralImage i) {
        List<FeatureK> list = new ArrayList<FeatureK>();

        for (Feature f : this) {
            list.add(new FeatureK(f, i));
        }

        return list;
    }
}
