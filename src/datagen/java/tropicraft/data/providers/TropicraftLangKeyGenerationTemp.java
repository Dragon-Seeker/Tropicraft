package tropicraft.data.providers;

import net.tropicraft.core.client.data.TropicraftLangKeys;

public class TropicraftLangKeyGenerationTemp {

    private static TropicraftLangKeys[] keys = TropicraftLangKeys.values();

    protected static void register(TropicraftLangProvider prov, TropicraftLangKeys lang) {
        prov.add(lang.getKey(), lang.getValue());
    }

    public static void generate(TropicraftLangProvider prov) {
        for (TropicraftLangKeys lang : keys) {
            register(prov, lang);
        }
    }
}
