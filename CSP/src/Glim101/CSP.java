package Glim101;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

//TODO:Le programme ne vérifie la cohérence des ensemble sur les contraintes ....


// Choix de codage 
//		Variable = String
//      Valeur = Object
//		un domaine = TreeSet<Object>
//		les couples (variable, domaine) = HashMap<String,TreeSet<Object>>
//		les contraintes = ArrayList<Constraint>

public class CSP {

	private HashMap<String, TreeSet<Object>> varDom; // table de hachage
														// associant � chaque
														// variable son domaine
	private ArrayList<Constraint> constraints; // liste des contraintes

	// initialise un CSP avec des structures vides
	public CSP() {
		varDom = new HashMap<String, TreeSet<Object>>();
		constraints = new ArrayList<Constraint>();
	}

	// initialise un CSP � partir d'un fichier texte
	public CSP(String fileName) throws Exception {
		varDom = new HashMap<String, TreeSet<Object>>();
		constraints = new ArrayList<Constraint>();

		BufferedReader lectureFichier = new BufferedReader(new FileReader(fileName));
		System.out.println("Leture du fichier " + fileName + " ...");
		String s = lectureFichier.readLine();

		if (s != null) {
			Integer nbrsVariable = Integer.parseInt(s);
			// les variables
			for (int i = 0; i < nbrsVariable; i++) {
				// les domaines
				s = lectureFichier.readLine();
				if (s != null) {
					String[] tabString = s.split(";");
					
					this.addVariable(tabString[0]);
					
					for (int j = 1; j < tabString.length; j++) {
						this.addValue(tabString[0], tabString[j]);
					}
					
					

				}
				else{
					throw new Exception("[ErrorFormat:Variable;Domaine] 'You did it wrong John ...'");
				}

			}

			// les contraintes
			ArrayList<String> varTuple;
			ArrayList<Object> valTuple;
			s = lectureFichier.readLine();
			if(s!=null){
				Integer nbrsContrainte = Integer.parseInt(s);
				for (int i = 0; i < nbrsContrainte; i++) {
					s = lectureFichier.readLine();
					if(s!=null)
					{
						varTuple = new ArrayList<String>();
						String[] tabString = s.split(";");
						//les variables des contraintes
						for (int j = 0; j < tabString.length; j++) {
							varTuple.add(j,tabString[j]);
						}
						Constraint Ctr = new Constraint(varTuple);
						
						s = lectureFichier.readLine();
						Integer nbrsTuple = Integer.parseInt(s);
						
						for (int j = 0; j < nbrsTuple; j++) {
							s = lectureFichier.readLine();
							tabString = s.split(";");
							valTuple = new ArrayList<Object>();
							for (int j2 = 0; j2 < tabString.length; j2++) {
								valTuple.add(j2,tabString[j2]);
							}
							
							Ctr.addTuple(valTuple);
							
							
						}
						
						this.addConstraint(Ctr);
	
					}
					else{
						throw new Exception("[ErrorFormat:varContrainte]'-__-'");
					}
				}
				
			}
			else{
				throw new Exception("[ErreurFormat:NbrsContrainte] 'Tu ne sais jamais ce que tu veux :/'");
			}
			
		}
		else{
			throw new Exception("[ErreurFormat:NombreVariable]'Dommage si seulement tu savais remplir un .txt'");
		}

		lectureFichier.close();
		System.out.println("Fin du fichier\n");

	}

	// ajoute une variable
	public void addVariable(String var) {
		if (varDom.get(var) == null)
			varDom.put(var, new TreeSet<Object>());
		else
			System.err.println("Variable " + var + " deja existante");
	}

	// ajoute une valeur au domaine d'une variable
	public void addValue(String var, Object val) {
		if (varDom.get(var) == null)
			System.err.println("Variable " + var + " non existante");
		else {
			TreeSet<Object> dom = varDom.get(var);
			if (!dom.add(val))
				/*
				 * varDom.put(var, dom); else
				 */System.err.println("La valeur " + val + " est deja dans le domaine de la variable " + var);
		}
	}

	// ajoute une contrainte
	public void addConstraint(Constraint c) {
		// on ne verifie pas que les valeurs des contraintes sont "compatibles"
		// avec les domaines
		if (!varDom.keySet().containsAll(c.getVariables()))
			System.err.println("La contrainte " + c.getName() + " contient des variables (" + c.getVariables()
					+ ") non declarees dans le CSP dont les variables sont " + varDom.keySet());
		else
			constraints.add(c);
	}

	public int getVarNumber() {
		return varDom.size();
	}

	public int getDomSize(String var) {
		return varDom.values().size();
	}

	public int getConstraintNumber() {
		return constraints.size();
	}

	public Set<String> getVar() {
		return varDom.keySet();
	}

	public TreeSet<Object> getDom(String var) {
		return varDom.get(var);
	}

	public HashMap<String, TreeSet<Object>> getDom() {
		return varDom;
	}

	public ArrayList<Constraint> getConstraints() {
		return constraints;
	}

	// retourne l'ensemble des contraintes contenant la variable pass�e en
	// param�tre
	public ArrayList<Constraint> getConstraintsContaining(String var) {
		ArrayList<Constraint> selected = new ArrayList<Constraint>();
		for (Constraint c : constraints) {
			if (c.getVariables().contains(var))
				selected.add(c);
		}
		return selected;
	}

	public String toString() {
		return "Var et Dom : " + varDom + "\nConstraints :" + constraints;
	}

	public static void main(String[] args){
		try {
			
		CSP monCSP = new CSP("src/Glim101/example.txt");
		
		System.out.println("\nMon CSP " + monCSP);
		} catch(Exception e){
			e.printStackTrace();
		}

	}

}
