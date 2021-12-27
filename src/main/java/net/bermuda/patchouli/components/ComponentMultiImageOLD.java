//package net.bermuda.patchouli.components;
//
//import com.google.gson.annotations.SerializedName;
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.bermuda.mixins.pageAddButtonInvoker;
//import net.minecraft.client.gui.GuiComponent;
//import net.minecraft.client.gui.components.Button;
//import net.minecraft.resources.ResourceLocation;
//import vazkii.patchouli.api.IVariable;
//import vazkii.patchouli.client.book.BookContentsBuilder;
//import vazkii.patchouli.client.book.BookEntry;
//import vazkii.patchouli.client.book.BookPage;
//import vazkii.patchouli.client.book.gui.GuiBookEntry;
//import vazkii.patchouli.client.book.gui.button.GuiButtonBookArrowSmall;
//import vazkii.patchouli.client.book.template.TemplateComponent;
//
//import java.util.ArrayList;
//import java.util.function.UnaryOperator;
//
//public class ComponentMultiImage extends TemplateComponent {
//    public IVariable[] images;
//    public int u;
//    public int v;
//    public int width;
//    public int height;
//    @SerializedName("texture_width")
//    public int textureWidth = 256;
//    @SerializedName("texture_height")
//    public int textureHeight = 256;
//    public float scale = 1.0F;
//    transient ArrayList<ResourceLocation> resources = new ArrayList<>();
//    transient int index;
//
//    public ComponentMultiImage() {
//    }
//
//    public void build(BookContentsBuilder builder, BookPage page, BookEntry entry, int pageNum) {
//        super.build(builder, page, entry, pageNum);
//        for(IVariable image : this.images){
//            resources.add(new ResourceLocation(image.asString()));
//        }
//    }
//
//    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
//        super.onVariablesAvailable(lookup);
//        for(int i = 0; i < this.images.length; ++i) {
//            this.images[i] = (IVariable)lookup.apply(this.images[i]);
//        }
//    }
//
//    public void render(PoseStack ms, BookPage page, int mouseX, int mouseY, float pticks) {
//        if (this.scale != 0.0F) {
//            RenderSystem.setShaderTexture(0, this.resources.get(this.index));
//            ms.pushPose();
//            ms.translate((double)this.x, (double)this.y, 0.0);
//            ms.scale(this.scale, this.scale, this.scale);
//            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//            RenderSystem.enableBlend();
//            GuiComponent.blit(ms, 0, 0, (float)this.u, (float)this.v, this.width, this.height, this.textureWidth, this.textureHeight);
//            ms.popPose();
//        }
//    }
//
//    public void onDisplayed(BookPage page, GuiBookEntry parent, int left, int top) {
//        super.onDisplayed(page, parent, left, top);
//        int x = 90;
//        int y = 100;
//        ((pageAddButtonInvoker)page).invokeAddButton(new GuiButtonBookArrowSmall(parent, x, y, true, () -> this.index > 0, this::handleButtonArrow));
//        ((pageAddButtonInvoker)page).invokeAddButton(new GuiButtonBookArrowSmall(parent, x + 10, y, false, () -> this.index < this.resources.size() - 1, this::handleButtonArrow));
//    }
//
//    public void handleButtonArrow(Button button) {
//        boolean left = ((GuiButtonBookArrowSmall)button).left;
//        if (left) {
//            --this.index;
//        } else {
//            ++this.index;
//        }
//
//    }
//
//}
