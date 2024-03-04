package service;

import domain.SearchEngine;
import repository.Repository;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Service {
    private Repository repo;

    public Service(Repository repo) {
        this.repo = repo;
    }

    public List<SearchEngine> getDocuments(){
        return repo.getDocuments();
    }


    public List<SearchEngine> getSorted(){

        return this.repo.getDocuments().stream()
                .sorted(Comparator.comparing(SearchEngine::getName))
                .collect(Collectors.toList());
    }

    public List<SearchEngine> searchByNameOrKeywords(String search){
        return this.repo.getDocuments().stream()
                .filter(r-> r.getName().contains(search)||r.getKeywords().contains(search))
//                || Objects.equals(r.getKeywords(),search))
                .collect(Collectors.toList());
    }

    public void updateDocument(SearchEngine searchEngine, String keyword, String content){
        searchEngine.setKeywords(keyword);
        searchEngine.setContents(content);
        try{
            this.repo.update(searchEngine);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public double bestMatching(String s1, String s2){
        return repo.bestMatching(s1,s2);
    }

    public SearchEngine findBestMatching(String search){
        return repo.findBestMath(search);
    }




}
