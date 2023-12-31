package mods.officialy.researchmod.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.datafixers.util.Either;
import mods.officialy.researchmod.ResearchMod;
import mods.officialy.researchmod.research.ResearchEntry;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ResearchParser {
    private final HolderLookup<ResearchEntry> research;
    private final StringReader reader;
    public Either<Holder<ResearchEntry>, HolderSet<ResearchEntry>> result;
    public Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions;

    private static final DynamicCommandExceptionType ERROR_UNKNOWN_RESEARCH = new DynamicCommandExceptionType((obj) -> Component.translatable("argument.research.id.invalid", obj));

    public ResearchParser(HolderLookup<ResearchEntry> research, StringReader reader) {
        super();
        this.research = research;
        this.reader = reader;
    }

    public void parse() throws CommandSyntaxException {
        this.suggestions = this::suggestResearch;
        this.readResearch();
    }

    private void readResearch() throws CommandSyntaxException {
        int i = this.reader.getCursor();
        ResourceLocation resourcelocation = ResourceLocation.read(this.reader);
        Optional<? extends Holder<ResearchEntry>> optional = this.research.get(ResourceKey.create(ResearchMod.RESEARCH_KEY, resourcelocation));
        this.result = Either.left(optional.orElseThrow(() -> {
            this.reader.setCursor(i);
            return ERROR_UNKNOWN_RESEARCH.createWithContext(this.reader, resourcelocation);
        }));
    }

    private CompletableFuture<Suggestions> suggestResearch(SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggestResource(this.research.listElementIds().map(ResourceKey::location), builder);
    }
}
