package com.deathmatch.genious.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.deathmatch.genious.domain.UnionCardDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UnionCombination {
	
	private final int SUB = 3;
	private final int TOTAL = 9;
	int index = 0;
	
	List<UnionCardDTO> answerSubCandidateList = new LinkedList<>();
	Set<UnionCardDTO[]> answerCandidateSet = new HashSet<>();
	
	public Set<UnionCardDTO[]> makeCombination(List<UnionCardDTO> problemList){
				
		Set<UnionCardDTO[]> transferAnswerCandidateSet = new HashSet<>();
		doCombination(problemList, TOTAL, SUB, index);
		
		transferAnswerCandidateSet = answerCandidateSet;
		answerCandidateSet = new HashSet<>();
		
		return transferAnswerCandidateSet;
	}
	
	public void doCombination(List<UnionCardDTO> problemCardList
			, int TOTAL, int SUB, int index){
		
		if(SUB == 0) {
			UnionCardDTO[] answerSubCandidateArray = new UnionCardDTO[3];
			
			for(int i=0; i < 3; i++) {
				answerSubCandidateArray[i] = answerSubCandidateList.get(i);
			}
			answerCandidateSet.add(answerSubCandidateArray);
			return;
		}
		
		if(index == TOTAL) return;
		
		answerSubCandidateList.add(problemCardList.get(index));
		doCombination(problemCardList, TOTAL, SUB-1, index+1);
		((LinkedList<UnionCardDTO>) answerSubCandidateList).removeLast();
		doCombination(problemCardList, TOTAL, SUB, index+1);
		
	}

}
