package org.hedgetech.fairylightsredux.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.ItemStack;
import org.hedgetech.fairylightsredux.platform.Services;
import org.hedgetech.fairylightsredux.server.item.FLRItems;
import org.hedgetech.fairylightsredux.server.item.crafting.FLRCraftingRecipes;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class HangingLightsTutorial {
    private static Stage stage = Stage.NONE;
    private static LocalPlayer player;
    private static Balloon balloon;

    public static void tick() {
        final Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.isPaused()) return;

        if (mc.player != player) {
            player = mc.player;
            setStage(Stage.byName(Services.CONFIG.tutorialProgress()));
        }

        final boolean hasLights = player.getInventory().contains(FLRCraftingRecipes.LIGHTS);
        if (stage == Stage.NONE && hasLights) {
            setStage(Stage.HANGING_LIGHTS);
        } else if (stage == Stage.HANGING_LIGHTS) {
            if (!hasLights) {
                setStage(Stage.NONE);
            } else if (player.getInventory().contains(stack -> stack.is(FLRItems.HANGING_LIGHTS)) || player.getStats().getValue(Stats.ITEM_CRAFTED.get(FLRItems.HANGING_LIGHTS)) > 0) {
                setStage(Stage.COMPLETE);
            }
        }
    }

    private static void setStage(final Stage next) {
        if (balloon != null) {
            balloon.hide();
            balloon = null;
        }

        stage = next;
        if (next == Stage.HANGING_LIGHTS) {
            balloon = new Balloon();
            Minecraft.getInstance().getToastManager().addToast(balloon);
        }

        Services.CONFIG.setTutorialProgress(next.name);
    }

    private enum Stage {
        NONE("none"),
        HANGING_LIGHTS("hanging_lights"),
        COMPLETE("complete");

        private final String name;

        Stage(final String name) {
            this.name = name;
        }

        private static Stage byName(final String name) {
            return Arrays.stream(values()).filter(stage -> stage.name.equals(name)).findFirst().orElse(NONE);
        }
    }

    private static final class Balloon implements Toast {
        private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("toast/tutorial");

        private final ItemStack icon = new ItemStack(FLRItems.HANGING_LIGHTS);
        private Visibility visibility = Visibility.SHOW;

        private void hide() {
            this.visibility = Visibility.HIDE;
        }

        @Override
        public @NotNull Visibility getWantedVisibility() {
            return this.visibility;
        }

        @Override
        public void update(final @NotNull ToastManager manager, final long time) {}

        @Override
        public void render(final GuiGraphics graphics, final @NotNull Font font, final long time) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
            graphics.renderFakeItem(this.icon, 8, 8);
            graphics.drawString(font, Component.translatable("tutorial.fairylightsredux.craft_hanging_lights.title"), 30, 7, 0xFF500050, false);
            graphics.drawString(font, Component.translatable("tutorial.fairylightsredux.craft_hanging_lights.description"), 30, 18, 0xFF000000, false);
        }
    }

    private HangingLightsTutorial() {}
}
