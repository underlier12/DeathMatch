package com.deathmatch.genious.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.deathmatch.genious.domain.UnionAnswerDTO;
import com.deathmatch.genious.domain.UnionCardDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UnionCombination {
	
	private final int TOTAL = 9;
	private final int SUB = 3;
	int index = 0;
	List<UnionCardDTO> answerSubCandidateList = new LinkedList<>();
	Set<UnionAnswerDTO> answerCandidateSet = new HashSet<>();
	
	public Set<UnionAnswerDTO> makeCombination(List<UnionCardDTO> problemCardList){
				
		Set<UnionAnswerDTO> transferAnswerCandidateSet = new HashSet<>();
		doCombination(problemCardList, TOTAL, SUB, index);
		
		transferAnswerCandidateSet = answerCandidateSet;
		answerCandidateSet = new HashSet<>();
		
		return transferAnswerCandidateSet;
	}
	
	public void doCombination(List<UnionCardDTO> problemCardList
			, int TOTAL, int SUB, int index){
		
		if(SUB == 0) {
			UnionAnswerDTO unionAnswerDTO = UnionAnswerDTO.builder()
					.card1(answerSubCandidateList.get(0))
					.card2(answerSubCandidateList.get(1))
					.card3(answerSubCandidateList.get(2))
					.build();
			answerCandidateSet.add(unionAnswerDTO);
			return;
		}
		
		if(index == TOTAL) return;
		
		answerSubCandidateList.add(problemCardList.get(index));
		doCombination(problemCardList, TOTAL, SUB-1, index+1);
		((LinkedList<UnionCardDTO>) answerSubCandidateList).removeLast();
		doCombination(problemCardList, TOTAL, SUB, index+1);
		
	}

}
