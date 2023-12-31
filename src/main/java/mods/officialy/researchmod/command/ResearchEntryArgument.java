package mods.officialy.researchmod.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import mods.officialy.researchmod.ResearchMod;
import mods.officialy.researchmod.research.ResearchEntry;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ResearchEntryArgument implements ArgumentType<ResearchEntry> {

    private final HolderLookup<ResearchEntry> research;

    public ResearchEntryArgument(CommandBuildContext pContext) {
        this.research = pContext.holderLookup(ResearchMod.RESEARCH_KEY);
    }

    public static ResearchEntryArgument research(CommandBuildContext context) {
        return new ResearchEntryArgument(context);
    }


    @Override
    public ResearchEntry parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();

        try {
            ResearchParser parser = new ResearchParser(research, reader);
            parser.parse();
            Holder<ResearchEntry> holder = parser.result.left().orElseThrow(() -> new IllegalStateException("Parser returned unexpected name"));
            return holder.get();
        } catch (CommandSyntaxException commandsyntaxexception) {
            reader.setCursor(i);
            throw commandsyntaxexception;
        }
    }

    public static <S> ResearchEntry getResearch(CommandContext<S> pContext, String name) {
        return pContext.getArgument(name, ResearchEntry.class);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        research.listElements().forEach((entry) -> builder.suggest(entry.get().getResearchName().toString()));
        return builder.buildFuture();
    }

}
