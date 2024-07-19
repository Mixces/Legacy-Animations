package com.mixces.legacyanimations.hook;

import com.mixces.legacyanimations.mixin.interfaces.BasicBakedModelInterface;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BasicBakedModel;
import net.minecraft.client.texture.Sprite;

public class GlintModelHook
{

	//todo: hijack model with custom sprite :/
//	public static BakedModel getGlint(BakedModel model)
//	{
//		return (BakedModel) BasicBakedModelInterface.Builder(model.useAmbientOcclusion(), model.isSideLit(), model.hasDepth(), model.getTransformation(), model.getOverrides());
//	}
//
//	public static class JustUV extends Sprite
//	{
//
//		public static final JustUV INSTANCE = new JustUV();
//
//		protected JustUV()
//		{
//			super(null, null, 1, 1, 1, 1);
//		}
//
//		@Override
//        public float getFrameU(float frame)
//		{
//			return -frame / 16;
//		}
//
//		@Override
//        public float getFrameV(float frame)
//		{
//			return frame / 16;
//		}
//
//	}

}
