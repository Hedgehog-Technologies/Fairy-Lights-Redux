package org.hedgetech.fairylightsredux;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import org.hedgetech.fairylightsredux.data.EntityFastenerAccessorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
	public static final String MOD_ID = "fairylightsredux";
	public static final String MOD_NAME = "Fairy Lights Redux";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static final ResourceLocation CONNECTION_TYPE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "connection_type");
	public static final ResourceLocation FASTENER = ResourceLocation.fromNamespaceAndPath(MOD_ID, "fastener");
	public static final ResourceLocation FEATURE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "feature");
	public static final ResourceLocation NETWORK = ResourceLocation.fromNamespaceAndPath(MOD_ID, "net");
	public static final ResourceLocation STRING_TYPE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "string_type");

	// @TODO - Not sure if this is the right spot
	public static final DataComponentType<EntityFastenerAccessorData> ENTITY_FASTENER_ACCESSOR_COMPONENT_TYPE = DataComponentType.<EntityFastenerAccessorData>builder()
			.persistent(EntityFastenerAccessorData.CODEC)
			.build();
}