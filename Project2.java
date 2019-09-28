//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:            Project2: MASTERMIND
// Files:            Project2.java
// Semester:         CS 302 Summer 2017
//
// Author:           Caspar Chen
// Email:            jcchen4@wisc.edu
// CS Login:         caspar
// Lecturer's Name:  Steve Earth
// Lab Section:      (none)
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name:     Wenbo Ouyang
// Partner Email:    wouyang4@wisc.edu
// Partner CS Login: (none)
// Lecturer's Name:  Steve Earth
// Lab Section:     
// 
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
//    _X_ Write-up states that Pair Programming is allowed for this assignment.
//    _X_ We have both read the CS302 Pair Programming policy.
//    _X_ We have registered our team prior to the team registration deadline.
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully 
// acknowledge and credit those sources of help here.  Instructors and TAs do 
// not need to be credited here, but tutors, friends, relatives, room mates 
// strangers, etc do.
//
// Persons:          (none)
// Online Sources:   (none)
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////
package steve;
/**
 * This program is a MasterMind game between a human and an AI, or both AIs, or both humans.
 * One, as the codeMaker, selects a four color secret code using empty, red, orange, yellow,
 * green, blue, and violet. The order is relevant and duplicates are allowed. The codeBreaker
 * has ten attempts to guess the secret code. After each guess, the codeMaker issues a black 
 * peg for each of the breaker's slots which matches both the location and position in the
 * secret code, and a white peg if the color is correct but not the location. If this game is
 * between AIs, it will only print out the number of games for the TRIALS set and then program 
 * will ask whether the user would like a restart. If the answer starts with n or N. The program
 * will print out the average game's turn length.
 * Bugs: no Bugs 
 * 
 * @author Caspar Chen  */
import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
public class Project2 {
	
		public static Scanner scnr = new Scanner(System.in);
		public static Scanner entryScnr;
		private static int SEED = 321;
		public static Random rangen = new Random(SEED);
		private static final int TRIALS = 100000;
		public static enum Color {EMPTY, RED, ORANGE, YELLOW, GREEN, BLUE, VIOLET}
		
/**
 * This method is to generate an ArrayList of Code which contains every possible Code
 * and takes no arguments
 * @return an ArrayList of Code which contains every possible Code
 */
	public static ArrayList<Code> allOpts(){
	ArrayList<Code> code = new ArrayList<Code>();
	for(int i=0;i<7;i++){
		for(int j=0;j<7;j++){
			for(int k=0;k<7;k++){
				for(int m=0;m<7;m++){
					code.add(new Code(new Color[] {Color.values()[i], 
				Color.values()[j], Color.values()[k], Color.values()[m]}));
		}
		}
		}
	}
	return code;
}

public static void main(String[] args){
	int turns, pegAns, totTurns = 0, numGames=0;
	String makerName, breakerName, entry;
	boolean makerAI, breakerAI, hasHumans, gameOver;
	ArrayList<Code> remaining=new ArrayList<Code>();
	char ans='Y';
	Code solution, myGuess =new Code(); //just filler for initialization
	do{
		System.out.print("Enter name for the codeMaker: ");
		makerName = scnr.nextLine();
	}while (makerName.length()==0);
	makerAI = makerName.length()>1 && //uses short-circuit
			makerName.substring(0, 2).toUpperCase().equals("AI");
	do{
		System.out.print("Enter a different name for the codeBreaker: ");
		breakerName = scnr.nextLine();
	}while (breakerName.length()==0 || makerName.equals(breakerName));
	breakerAI = breakerName.length()>1 && 
			breakerName.substring(0, 2).toUpperCase().equals("AI");
	//gameplay starts here
	hasHumans = !makerAI || !breakerAI|| numGames>=TRIALS;;
	while(!hasHumans&&numGames<TRIALS || hasHumans&&ans =='Y'){
		remaining = allOpts();
		turns=0;
		pegAns=0;
		gameOver = false;
		System.out.println("\nPlaying game #"+ ++numGames);
		hasHumans = !makerAI || !breakerAI || numGames>=TRIALS;
		solution = Provided.randColors();
		if(makerAI){
			if(hasHumans){
			System.out.println(makerName+" has made a code for "+breakerName+" to break.");
			}
		}else{
			System.out.println(makerName+" should mentally make a code for "
						+breakerName+" to break.");
		}
		do{
			turns++;
			if(hasHumans)System.out.println("\nThis is game#"+numGames+", turn #"+ turns);
			if(breakerAI){
				if(turns==1){
					//myGuess = Provided.allSame();
					myGuess = Provided.twoDubs();//TODO: can change this as needed
				}else{//note that myGuess and pegAns exist from previous turn
					myGuess = myGuess.pickMove(remaining, pegAns); 
					if(myGuess==null){ //only happens if human maker in error/lies
						System.out.println(breakerName+" cannot make guess. "+
									"Inconsistent peg input. Terminating game");
						gameOver=true;
					}
				}
				if(hasHumans && !(myGuess==null)){
					System.out.println(breakerName+" entered "+myGuess);
				}
			}else{ //random from remaining arraylist, unless turn1
				myGuess = Provided.inputColors();
			}
			if(!gameOver){
				if(makerAI){
					pegAns = myGuess.pegs(solution);
				}else{
					pegAns = Provided.inputPegs();
				}
				if(hasHumans)System.out.println(breakerName+" guessed "+myGuess+", and "+
						makerName+" declares: Black = "+pegAns/10+", White = "+pegAns%10);
				if(pegAns==40){
					gameOver = true;
					if(hasHumans) System.out.println(breakerName+" broke the code of "
							+makerName+" on turn#"+turns);
				}else if(turns==10){
					gameOver = true;
					if(hasHumans){
						System.out.println(breakerName+" was unable to break the "+"code of "+makerName+
								" within the ten turn limit");
						if(makerAI){
							System.out.println("The solution was "+solution);
						} else{
							System.out.println("The solution will be verbally revealed by "+makerName+" now");
						}
					}
				}
			}
		} while(!gameOver);
		if(hasHumans){
			do{
				System.out.print("Do you wish to play again (Y/N)? ");
				entry = scnr.nextLine();
			}while(entry.length()==0);
			ans = entry.toUpperCase().charAt(0);
		}
		totTurns+=turns;
		turns=0;
	}
	System.out.println("\nThe average game's turn length was "+(double) totTurns/numGames);
}
}
