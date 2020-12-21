package me.jumper251.replay.utils.fetcher;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.annotations.SerializedName;

public abstract class JsonClass implements ExclusionStrategy {

	@Override
	public boolean shouldSkipClass (Class<?> arg0) {
		return false;
	}

	@Override
	public boolean shouldSkipField (FieldAttributes field) {
		SerializedName ns = field.getAnnotation (SerializedName.class);
		if (ns != null)
			return false;
		return true;
	}
}
