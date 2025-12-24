package org.hedgetech.fairylightsredux;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
	public static final String MOD_ID = "fairylightsredux";
	public static final String MOD_NAME = "Fairy Lights Redux";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static final ResourceLocation CONNECTION_TYPE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "connection_type");
	public static final ResourceLocation FEATURE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "feature");
	public static final ResourceLocation NETWORK = ResourceLocation.fromNamespaceAndPath(MOD_ID, "net");
	public static final ResourceLocation STRING_TYPE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "string_type");
}