package pl.jw.dictionary.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TranslationServiceImpl implements TranslationService {
    private Map<String, String> tmpDictionary = Map.of(
            "Testowanie", "Testing",
            "jest", "is",
            "ważne", "important",
            "śćźż", "Polish letters");//TODO needed to provide dictionary from file

    @Override
    public String translate(String sentenceToTranslate) {
        List<String> translatedSentence = translateSentenceToList(sentenceToTranslate);
        return createSentence(translatedSentence);
    }

    private List<String> translateSentenceToList(String sentence) {
        String[] splittedSentenceArray = sentence.split("\\b");
        return Arrays.stream(splittedSentenceArray)
                .map(this::translateSingleWord)
                .collect(Collectors.toList());
    }

    private String translateSingleWord(String sentencePart) {
        if (isWord(sentencePart)) {
            String translatedWord = "";
            Optional<String> dictionaryKeyOpt = tmpDictionary
                    .keySet()
                    .stream()
                    .filter(key -> key.toLowerCase().equals(sentencePart.toLowerCase()))
                    .findFirst();
            if (dictionaryKeyOpt.isPresent()) {
                translatedWord = tmpDictionary.get(dictionaryKeyOpt.get());
            }
            return translatedWord.isEmpty() ? "|" + sentencePart + " - not found in dictionary" + "|" : translatedWord;
        } else {
            return sentencePart;
        }
    }

    private String createSentence(List<String> sentencePartsList) {
        StringBuilder sentenceBuilder = new StringBuilder();
        sentencePartsList.forEach(sentenceBuilder::append);
        return sentenceBuilder.toString();
    }

    private boolean isWord(String sentencePart) {
        if (sentencePart.isEmpty()) {
            return false;
        } else {
            return sentencePart.substring(0, 1).matches("[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]*");
        }
    }
}
