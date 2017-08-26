package net.tropicraft.core.common.entity.underdasea;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.tropicraft.Info;
import net.tropicraft.core.common.enums.TropicraftCorals;

public enum StarfishType {
	/** The red knobbed starfish. */
	RED("starfishRed", "Red Starfish", new String[] {
		"starfish_red_0.png",
		"starfish_red_1.png",
		"starfish_red_2.png"
	}, new float[] {
		0.03125f,
		0.03125f/2.0f,
		0.03125f/2.0f
	}),
	/** Gorgeous 5 limbed starfish. Blue with orange rim. */
	ROYAL("starfishRoyal", "Royal Starfish", new String[] {
		"starfish_royal_0.png",
		"starfish_royal_1.png"
	});

	StarfishType(String unlocalizedName, String displayName, String[] textures, float[] heights) {
		this.unlocalizedName = unlocalizedName;
		this.displayName = displayName;

		if (heights == null) {
			this.layerHeights = new float[textures.length];
			for (int i = 0; i < textures.length; i++) {
				this.layerHeights[i] = 0.03125f;
			}
		} else {
			this.layerHeights = heights;
		}

		this.texturePaths = new ArrayList<String>(textures.length);
		
		for (String texture: textures) {
			this.texturePaths.add(Info.MODID + ":textures/entity/" + texture);
		}
	}

	StarfishType(String unlocalizedName, String displayName, String[] textures) {
		this(unlocalizedName, displayName, textures, null);
	}
	
	public static final StarfishType VALUES[] = values();
	
	public static byte getMetaFromType(StarfishType type) {
		return (byte)(type == RED ? 0 : 1);
	}
	
	private String unlocalizedName;
	private String displayName;
	private List<String> texturePaths;
	private float[] layerHeights;

	public String getUnlocalizedName() {
		return unlocalizedName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public List<String> getTexturePaths() {
		return texturePaths;
	}
	
	public float[] getLayerHeights() {
		return layerHeights;
	}
	
	public int getLayerCount() {
		return texturePaths.size();
	}
	
	public static StarfishType getRandomType() {
		int type = new Random().nextInt(2);
		if (type == 0) return StarfishType.RED;
		else return StarfishType.ROYAL;
	}
}