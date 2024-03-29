package com.ecmoho.jpush.api.push.model.audience;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.ecmoho.jpush.api.push.model.PushModel;
import com.ecmoho.jpush.api.push.model.audience.*;

import com.ecmoho.jpush.api.utils.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class AudienceTarget implements PushModel {
    private final com.ecmoho.jpush.api.push.model.audience.AudienceType audienceType;
    private final Set<String> values;
    
    private AudienceTarget(com.ecmoho.jpush.api.push.model.audience.AudienceType audienceType, Set<String> values) {
        this.audienceType = audienceType;
        this.values = values;
    }
    
    public static Builder newBuilder() {
        return new Builder();
    }
    
    public static AudienceTarget tag(String... tag) {
        return newBuilder().setAudienceType(com.ecmoho.jpush.api.push.model.audience.AudienceType.TAG).addAudienceTargetValues(tag).build();
    }
    
    public static AudienceTarget tag(Collection<String> tags) {
        return newBuilder().setAudienceType(com.ecmoho.jpush.api.push.model.audience.AudienceType.TAG).addAudienceTargetValues(tags).build();
    }
    
    public static AudienceTarget tag_and(String... tag) {
        return newBuilder().setAudienceType(com.ecmoho.jpush.api.push.model.audience.AudienceType.TAG_AND).addAudienceTargetValues(tag).build();
    }
    
    public static AudienceTarget tag_and(Collection<String> tags) {
        return newBuilder().setAudienceType(com.ecmoho.jpush.api.push.model.audience.AudienceType.TAG_AND).addAudienceTargetValues(tags).build();
    }
    
    public static AudienceTarget alias(String... alias) {
        return newBuilder().setAudienceType(com.ecmoho.jpush.api.push.model.audience.AudienceType.ALIAS).addAudienceTargetValues(alias).build();
    }
    
    public static AudienceTarget alias(Collection<String> aliases) {
        return newBuilder().setAudienceType(com.ecmoho.jpush.api.push.model.audience.AudienceType.ALIAS).addAudienceTargetValues(aliases).build();
    }

    public static AudienceTarget registrationId(String... registrationId) {
        return newBuilder().setAudienceType(com.ecmoho.jpush.api.push.model.audience.AudienceType.REGISTRATION_ID).addAudienceTargetValues(registrationId).build();
    }
    
    public static AudienceTarget registrationId(Collection<String> registrationIds) {
        return newBuilder().setAudienceType(com.ecmoho.jpush.api.push.model.audience.AudienceType.REGISTRATION_ID).addAudienceTargetValues(registrationIds).build();
    }
    
    
    public com.ecmoho.jpush.api.push.model.audience.AudienceType getAudienceType() {
        return this.audienceType;
    }
    
    public String getAudienceTypeValue() {
        return this.audienceType.value();
    }
    
    public JsonElement toJSON() {
        JsonArray array = new JsonArray();
		if (null != values) {
			for (String value : values) {
				array.add(new JsonPrimitive(value));
			}
		}
        return array;
    }
    
    
    public static class Builder {
        private com.ecmoho.jpush.api.push.model.audience.AudienceType audienceType = null;
        private Set<String> valueBuilder = null;
        
        public Builder setAudienceType(com.ecmoho.jpush.api.push.model.audience.AudienceType audienceType) {
            this.audienceType = audienceType;
            return this;
        }
        
        public Builder addAudienceTargetValue(String value) {
            if (null == valueBuilder) {
                valueBuilder = new HashSet<String>();
            }
            valueBuilder.add(value);
            return this;
        }
        
        public Builder addAudienceTargetValues(Collection<String> values) {
            if (null == valueBuilder) {
                valueBuilder = new HashSet<String>();
            }
            for (String value : values) {
                valueBuilder.add(value);
            }
            return this;
        }
        
        public Builder addAudienceTargetValues(String... values) {
            if (null == valueBuilder) {
                valueBuilder = new HashSet<String>();
            }
            for (String value : values) {
                valueBuilder.add(value);
            }
            return this;
        }
        
        public AudienceTarget build() {
            Preconditions.checkArgument(null != audienceType, "AudienceType should be set.");
            Preconditions.checkArgument(null != valueBuilder, "Target values should be set one at least.");
            return new AudienceTarget(audienceType, valueBuilder);
        }

        
    }
}
