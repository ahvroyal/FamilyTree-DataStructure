package com.company;

import java.util.*;

// Author : Amirreza - ahvroyal

/*
    Description :
    We are creating a family tree database program that stores the data
    of each person + the relationship among them.

*/

public class FamilyTree {

    Person root;

    int familyTreeSize = 0;
    int malePersonCount = 0;
    int femalePersonCount = 0;
    int marriedPersonCount = 0;
    int singlePersonCount = 0;
    int familyTreeDepth = 0;

    public static void main(String[] args) {

        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("            *****   Welcome To Family Tree Database Program   *****            ");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("Program functionality and instructions are declared in the user manual pdf." + '\n');
        System.out.println("Moreover functionality is as follows :" +
                '\n' + '\t' + "Press \"D\" to draw the current family tree on the terminal." + '\n');
        System.out.println("At any step press \"Q\" to quit the program.");
        System.out.println("Waiting for input ...");


        FamilyTree familyTree = new FamilyTree();

        Scanner scanner = new Scanner(System.in);
        String input;

        do {
            input = scanner.nextLine();

            if (input.equals("")) {
                System.out.println("input invalid, try again.");
            }
            else if (input.contains("ROOT")) {
                familyTree.addRoot(input);
            }
            else if (input.contains("CHILD ")) {
                familyTree.addChild(input);
            }
            else if (input.startsWith("SPOUSE")) {
                familyTree.addSpouse(input);
            }
            else if (input.contains("STATS")) {
                familyTree.showStats();
            }
            else if (input.contains("BIGGEST")) {
                familyTree.showBiggestFamily();
            }
            else if (input.contains("PARENTS OF")) {
                familyTree.parentsOf(input);
            }
            else if (input.contains("CHILDREN OF")) {
                familyTree.childrenOf(input);
            }
            else if (input.contains("FATHER’S BROTHERS OF")) {
                familyTree.fathersBrothersOf(input);
            }
            else if (input.contains("FATHER’S SISTERS OF")) {
                familyTree.fathersSistersOf(input);
            }
            else if (input.contains("MOTHER’S BROTHERS OF")) {
                familyTree.mothersBrothersOf(input);
            }
            else if (input.contains("MOTHER’S SISTERS OF")) {
                familyTree.mothersSistersOf(input);
            }
            else if (input.contains("UNCLES OF")) {
                familyTree.unclesOf(input);
            }
            else if (input.contains("AUNTS OF")) {
                familyTree.auntsOf(input);
            }
            else if (input.contains("COUSINS OF")) {
                familyTree.cousinsOf(input);
            }
            else if (input.contains("IS SPOUSE OF")) {
                familyTree.spouseOf(input);
            }
            else if (input.contains("ARE BROTHERS OF")) {
                familyTree.brothersOf(input);
            }
            else if (input.contains("ARE SISTERS OF")) {
                familyTree.sistersOf(input);
            }
            else if (input.equals("D")) {
                familyTree.drawTree();
            }
            else {
                if (!input.equals("Q")) {
                    System.out.println("input invalid, try again.");
                }
            }

        }while (!input.equals("Q"));

        System.out.println("Farewell !");
        System.exit(0);

    }


    public void addRoot(String data) {

        String[] details = data.split(" ");

        if (root != null) {
            System.out.println("root is already defined !");
        }
        else {
            root = new Person(details[1],details[2]);
        }
    }


    public void addChild(String data) {

        if (root == null) {
            System.out.println("the root of the family is yet to be defined !");
            return;
        }

        String[] details = data.split(" ");

        if (identicalNames(details[1])) {
            System.out.println("names have to be distinct in this family tree.");
            return;
        }

        Person firstParent = searchSpecifiedPerson(details[4]);

        if (firstParent == null) {
            System.out.println("the parent of this children does not exist in the family tree.");
            return;
        }

        if (firstParent.partner == null) {
            System.out.println("one of the parents of the child you want to add is not defined.");
            return;
        }

        Person secondParent = firstParent.partner;

        Person child = new Person(details[1],details[2],firstParent);

        firstParent.children.add(child);
        secondParent.children.add(child);

    }


    public void addSpouse(String data) {

        if (root == null) {
            System.out.println("the root of the family is yet to be defined !");
            return;
        }

        String[] details = data.split(" ");

        if (identicalNames(details[4])) {
            System.out.println("names have to be distinct in this family tree.");
            return;
        }

        Person spouseIncluded = searchSpecifiedPerson(details[2]);

        if (spouseIncluded == null) {
            System.out.println("the spouse of person you stated does not exist in the family tree.");
            return;
        }

        if (spouseIncluded.partner != null) {
            System.out.println("this person is currently in relationship !");
            return;
        }

        Person checkForNotExisting = searchSpecifiedPerson(details[4]);
        if (checkForNotExisting != null) {
            System.out.println("marriage within the family is prohibited !");
            return;
        }

        String gender = (spouseIncluded.gender.equals("MALE")) ? "FEMALE" : "MALE";

        Person spouseNew = new Person(details[4],gender);

        spouseIncluded.partner = spouseNew;
        spouseNew.partner = spouseIncluded;

    }


    public void showStats() {

        if (root == null) {
            System.out.println("the root of the family is yet to be defined !");
            return;
        }

        traversalUpdate();
        System.out.println("family tree size : " + familyTreeSize +
                    '\n' + "male person count : " + malePersonCount +
                    '\n' + "female person count : " + femalePersonCount +
                    '\n' + "married person count : " + marriedPersonCount +
                    '\n' + "single person count : " + singlePersonCount +
                    '\n' + "family tree depth : " + familyTreeDepth);

    }


    public void showBiggestFamily() {

        if (root == null) {
            System.out.println("the root of the family is yet to be defined !");
            return;
        }

        Queue<Person> people = new ArrayDeque<>();
        people.add(root);

        Person maxChildren = null;
        int maxChildrenCount = 0;

        while (!people.isEmpty()) {

            int size = people.size();

            while (size > 0) {

                Person temp = people.poll();

                assert temp != null;
                if (temp.children.size() > maxChildrenCount) {
                    maxChildrenCount = temp.children.size();
                    maxChildren = temp;
                }

                people.addAll(temp.children);

                size--;
            }
        }

        if (maxChildren != null) {
            System.out.println("biggest family belongs to : " + maxChildren.name);
            System.out.print("his/her children are : ");
            for (Person person : maxChildren.children) {
                System.out.print(person.name);
                System.out.print(" ");
            }
            System.out.println();
        }
        else {
            System.out.println("no person inside this family tree has any child !");
        }

    }


    public void parentsOf(String data) {

        if (root == null) {
            System.out.println("the root of the family is yet to be defined !");
            return;
        }

        String[] details = data.split(" ");
        Person underScope = searchSpecifiedPerson(details[4]);

        if (underScope == null) {
            System.out.println("such person doesn't exist !");
        }

        else {

            if (underScope.parents[0] == null || underScope.parents[1] == null) {
                System.out.println("such person doesn't have any parents ! (probably root or a spouse)");
            }
            else {
                System.out.println("Parents of " + underScope.name + " are : " +
                        underScope.parents[0].name + " & " + underScope.parents[1].name);
            }

        }

    }


    public void childrenOf(String data) {

        if (root == null) {
            System.out.println("the root of the family is yet to be defined !");
            return;
        }

        String[] details = data.split(" ");
        Person underScope = searchSpecifiedPerson(details[4]);

        if (underScope == null) {
            System.out.println("such person doesn't exist !");
        }
        else {

            if (underScope.children.size() == 0) {
                System.out.println("that person doesn't have any children.");
            }
            else {

                System.out.print("Children of " + underScope.name + " are : ");

                for (Person person : underScope.children) {
                    System.out.print(person.name);
                    System.out.print(" ");
                }

                System.out.println();
            }

        }

    }


    public void fathersBrothersOf(String data) {

        if (root == null) {
            System.out.println("the root of the family is yet to be defined !");
            return;
        }

        String[] details = data.split(" ");
        Person underScope = searchSpecifiedPerson(details[5]);

        if (underScope == null) {
            System.out.println("such person doesn't exist !");
        }

        else {

            if (underScope.parents[0] == null || underScope.parents[1] == null) {
                System.out.println("such person doesn't have any parents ! (probably root or a spouse)");
                return;
            }

            if (underScope.parents[0].gender.equals("FEMALE")) {
                System.out.println("due to restrictions on our family tree, information on a person spouse doesn't neither stored nor processed.");
                return;
            }

            StringBuilder results = new StringBuilder();

            traversalUpdate();
            if (underScope.depth == 1) {
                System.out.println("his/her parent is root, therefore no such information.");
                return;
            }

            for (Person person : underScope.parents[0].parents[0].children) {
                if (!person.name.equals(underScope.parents[0].name) && person.gender.equals("MALE"))
                    results.append(person.name).append(" ");

            }

            if (results.length() == 0) {
                System.out.println(underScope.name + " doesn't have have any father's brother.");
                return;
            }

            System.out.print("Father's brothers of " + underScope.name + " are : ");
            System.out.println(results);

        }

    }


    public void fathersSistersOf(String data) {

        if (root == null) {
            System.out.println("the root of the family is yet to be defined !");
            return;
        }

        String[] details = data.split(" ");
        Person underScope = searchSpecifiedPerson(details[5]);

        if (underScope == null) {
            System.out.println("such person doesn't exist !");
        }

        else {

            if (underScope.parents[0] == null || underScope.parents[1] == null) {
                System.out.println("such person doesn't have any parents ! (probably root or a spouse)");
                return;
            }

            if (underScope.parents[0].gender.equals("FEMALE")) {
                System.out.println("due to restrictions on our family tree, information on a person spouse doesn't neither stored nor processed.");
                return;
            }

            StringBuilder results = new StringBuilder();

            traversalUpdate();
            if (underScope.depth == 1) {
                System.out.println("his/her parent is root, therefore no such information.");
                return;
            }

            for (Person person : underScope.parents[0].parents[0].children) {
                if (!person.name.equals(underScope.parents[0].name) && person.gender.equals("FEMALE"))
                    results.append(person.name).append(" ");

            }

            if (results.length() == 0) {
                System.out.println(underScope.name + " doesn't have have any father's sister.");
                return;
            }

            System.out.print("Father's sisters of " + underScope.name + " are : ");
            System.out.println(results);

        }

    }


    public void mothersBrothersOf(String data) {

        if (root == null) {
            System.out.println("the root of the family is yet to be defined !");
            return;
        }

        String[] details = data.split(" ");
        Person underScope = searchSpecifiedPerson(details[5]);

        if (underScope == null) {
            System.out.println("such person doesn't exist !");
        }

        else {

            if (underScope.parents[0] == null || underScope.parents[1] == null) {
                System.out.println("such person doesn't have any parents ! (probably root or a spouse)");
                return;
            }

            if (underScope.parents[0].gender.equals("MALE")) {
                System.out.println("due to restrictions on our family tree, information on a person spouse doesn't neither stored nor processed.");
                return;
            }

            StringBuilder results = new StringBuilder();

            traversalUpdate();
            if (underScope.depth == 1) {
                System.out.println("his/her parent is root, therefore no such information.");
                return;
            }

            for (Person person : underScope.parents[0].parents[0].children) {
                if (!person.name.equals(underScope.parents[0].name) && person.gender.equals("MALE"))
                    results.append(person.name).append(" ");

            }

            if (results.length() == 0) {
                System.out.println(underScope.name + " doesn't have have any mother's brother.");
                return;
            }

            System.out.print("Mother's brothers of " + underScope.name + " are : ");
            System.out.println(results);

        }

    }


    public void mothersSistersOf(String data) {

        if (root == null) {
            System.out.println("the root of the family is yet to be defined !");
            return;
        }

        String[] details = data.split(" ");
        Person underScope = searchSpecifiedPerson(details[5]);

        if (underScope == null) {
            System.out.println("such person doesn't exist !");
        }

        else {

            if (underScope.parents[0] == null || underScope.parents[1] == null) {
                System.out.println("such person doesn't have any parents ! (probably root or a spouse)");
                return;
            }

            if (underScope.parents[0].gender.equals("MALE")) {
                System.out.println("due to restrictions on our family tree, information on a person spouse doesn't neither stored nor processed.");
                return;
            }

            StringBuilder results = new StringBuilder();

            traversalUpdate();
            if (underScope.depth == 1) {
                System.out.println("his/her parent is root, therefore no such information.");
                return;
            }

            for (Person person : underScope.parents[0].parents[0].children) {
                if (!person.name.equals(underScope.parents[0].name) && person.gender.equals("FEMALE"))
                    results.append(person.name).append(" ");

            }

            if (results.length() == 0) {
                System.out.println(underScope.name + " doesn't have have any mother's sister.");
                return;
            }

            System.out.print("Mother's sisters of " + underScope.name + " are : ");
            System.out.println(results);

        }

    }


    public void unclesOf(String data) {

        if (root == null) {
            System.out.println("the root of the family is yet to be defined !");
            return;
        }

        String[] details = data.split(" ");
        Person underScope = searchSpecifiedPerson(details[4]);

        if (underScope == null) {
            System.out.println("such person doesn't exist !");
        }

        else {

            if (underScope.parents[0] == null || underScope.parents[1] == null) {
                System.out.println("such person doesn't have any parents ! (probably root or a spouse)");
                return;
            }

            traversalUpdate();
            if (underScope.depth == 1) {
                System.out.println("his/her parent is root, therefore no such information.");
                return;
            }

            StringBuilder results = new StringBuilder();

            for (Person person : underScope.parents[0].parents[0].children) {
                if (!person.name.equals(underScope.parents[0].name) && person.gender.equals("MALE"))
                    results.append(person.name).append(" ");

                if (person.partner != null) {
                    if (!person.name.equals(underScope.parents[0].name) && person.partner.gender.equals("MALE"))
                        results.append(person.partner.name).append(" ");

                }
            }

            if (results.length() == 0) {
                System.out.println(underScope.name + " doesn't have have any uncles.");
                return;
            }

            System.out.print("Uncles of " + underScope.name + " are : ");
            System.out.println(results);
        }

    }


    public void auntsOf(String data) {

        if (root == null) {
            System.out.println("the root of the family is yet to be defined !");
            return;
        }

        String[] details = data.split(" ");
        Person underScope = searchSpecifiedPerson(details[4]);

        if (underScope == null) {
            System.out.println("such person doesn't exist !");
        }

        else {

            if (underScope.parents[0] == null || underScope.parents[1] == null) {
                System.out.println("such person doesn't have any parents ! (probably root or a spouse)");
                return;
            }

            traversalUpdate();
            if (underScope.depth == 1) {
                System.out.println("his/her parent is root, therefore no such information.");
                return;
            }

            StringBuilder results = new StringBuilder();

            for (Person person : underScope.parents[0].parents[0].children) {
                if (!person.name.equals(underScope.parents[0].name) && person.gender.equals("FEMALE"))
                    results.append(person.name).append(" ");

                if (person.partner != null) {
                    if (!person.name.equals(underScope.parents[0].name) && person.partner.gender.equals("FEMALE"))
                        results.append(person.partner.name).append(" ");
                }

            }

            if (results.length() == 0) {
                System.out.println(underScope.name + " doesn't have have any aunts.");
                return;
            }

            System.out.print("Aunts of " + underScope.name + " are : ");
            System.out.println(results);

        }

    }


    public void cousinsOf(String data) {

        if (root == null) {
            System.out.println("the root of the family is yet to be defined !");
            return;
        }

        String[] details = data.split(" ");
        Person underScope = searchSpecifiedPerson(details[4]);

        if (underScope == null) {
            System.out.println("such person doesn't exist !");
        }

        else {

            if (underScope.parents[0] == null || underScope.parents[1] == null) {
                System.out.println("such person doesn't have any parents ! (probably root or a spouse)");
                return;
            }

            traversalUpdate();
            if (underScope.depth == 1) {
                System.out.println("his/her parent is root, therefore no such information.");
                return;
            }

            StringBuilder results = new StringBuilder();

            int targetDepth = underScope.depth;

            Queue<Person> people = new ArrayDeque<>();
            people.add(root);

            while (!people.isEmpty()) {

                int size = people.size();

                while (size > 0) {

                    Person temp = people.poll();

                    assert temp != null;
                    if (temp.depth == targetDepth) {
                        if (!temp.name.equals(details[4]))
                            if (!temp.parents[0].equals(searchSpecifiedPerson(details[4]).parents[0]))
                                results.append(temp.name).append(" ");
                    }

                    people.addAll(temp.children);

                    size--;
                }

            }

            System.out.print("Cousins of " + underScope.name + " are : ");
            System.out.println(results);

        }

    }


    public void spouseOf(String data) {

        if (root == null) {
            System.out.println("the root of the family is yet to be defined !");
            return;
        }

        String[] details = data.split(" ");
        Person underScope = searchSpecifiedPerson(details[4]);

        if (underScope == null) {
            System.out.println("such person doesn't exist !");
        }

        else {

            if (underScope.partner == null) {
                System.out.println("this person is single.");
            }
            else {
                System.out.println("Spouse of " + underScope.name + " is : " + underScope.partner.name);
            }

        }

    }


    public void brothersOf(String data) {

        if (root == null) {
            System.out.println("the root of the family is yet to be defined !");
            return;
        }

        String[] details = data.split(" ");
        Person underScope = searchSpecifiedPerson(details[4]);

        if (underScope == null) {
            System.out.println("such person doesn't exist !");
        }

        else {

            if (underScope.parents[0] == null || underScope.parents[1] == null) {
                System.out.println("such person doesn't have any parents ! (probably root or a spouse)");
                return;
            }

            StringBuilder results = new StringBuilder();

            for (Person person : underScope.parents[0].children) {
                if (!person.name.equals(underScope.name) && person.gender.equals("MALE"))
                    results.append(person.name).append(" ");
            }

            if (results.length() == 0) {
                System.out.println(underScope.name + " doesn't have have any brothers.");
                return;
            }

            System.out.print("Brothers of " + underScope.name + " are : ");
            System.out.println(results);

        }

    }


    public void sistersOf(String data) {

        if (root == null) {
            System.out.println("the root of the family is yet to be defined !");
            return;
        }

        String[] details = data.split(" ");
        Person underScope = searchSpecifiedPerson(details[4]);

        if (underScope == null) {
            System.out.println("such person doesn't exist !");
        }

        else {

            if (underScope.parents[0] == null || underScope.parents[1] == null) {
                System.out.println("such person doesn't have any parents ! (probably root or a spouse)");
                return;
            }

            StringBuilder results = new StringBuilder();

            for (Person person : underScope.parents[0].children) {
                if (!person.name.equals(underScope.name) && person.gender.equals("FEMALE"))
                    results.append(person.name).append(" ");
            }

            if (results.length() == 0) {
                System.out.println(underScope.name + " doesn't have have any sisters.");
                return;
            }

            System.out.print("Sisters of " + underScope.name + " are : ");
            System.out.println(results);

        }

    }


    public Person searchSpecifiedPerson(String name) {

        if (root == null)
            return null;

        Queue<Person> people = new ArrayDeque<>();
        people.add(root);

        while (!people.isEmpty()) {

            int size = people.size();

            while (size > 0) {

                Person temp = people.poll();

                if (Objects.requireNonNull(temp).name.equals(name))
                    return temp;

                if (temp.partner != null) {

                    if (temp.partner.name.equals(name))
                        return temp.partner;

                }

                people.addAll(temp.children);

                size--;
            }

        }

        return null;
    }


    public void traversalUpdate() {

        if (root == null)
            return;

        Queue<Person> people = new ArrayDeque<>();
        people.add(root);

        int level = 0;
        int familySize = 0;
        int maleCount = 0;
        int femaleCount = 0;
        int marriedCount = 0;
        int singleCount = 0;

        root.depth = level;

        while (!people.isEmpty()) {

            int currentLevelSize = people.size();

            while (currentLevelSize > 0) {

                Objects.requireNonNull(people.peek()).depth = level;

                Person temp = people.poll();

                familySize++;

                if (Objects.requireNonNull(temp).gender.equals("MALE"))
                    maleCount++;
                else
                    femaleCount++;

                if (temp.partner != null) {

                    temp.partner.depth = level;

                    familySize++;
                    if (temp.partner.gender.equals("MALE"))
                        maleCount++;
                    else
                        femaleCount++;

                    marriedCount+=2;
                }
                else
                    singleCount++;


                people.addAll(temp.children);

                currentLevelSize--;
            }

            level++;

        }

        familyTreeDepth = level-1;
        familyTreeSize = familySize;
        malePersonCount = maleCount;
        femalePersonCount = femaleCount;
        marriedPersonCount = marriedCount;
        singlePersonCount = singleCount;

    }


    public boolean identicalNames(String name) {

        return searchSpecifiedPerson(name) != null;
    }


    public void drawTree() {

        if (root == null) {
            System.out.println("the family tree is empty !");
            return;
        }

        System.out.println(root.toString());

    }


    // in object oriented approach we are creating entities as Person class
    static class Person {

        String name;
        String gender;
        Person partner;
        Person[] parents = new Person[2];
        ArrayList<Person> children;
        int depth;


        public Person(String name ,String gender ,Person ancestryParent) {

            this.name = name;
            this.gender = gender;
            partner = null;
            parents[0] = ancestryParent;
            parents[1] = ancestryParent.partner;
            children = new ArrayList<>();
            depth = 0;

        }


        public Person(String name ,String gender) {

            this.name = name;
            this.gender = gender;
            partner = null;
            parents[0] = parents [1] = null;
            children = new ArrayList<>();
            depth = 0;

        }


        @Override
        public String toString() {
            StringBuilder buffer = new StringBuilder(50);
            print(buffer, "", "");
            return buffer.toString();
        }

        private void print(StringBuilder buffer, String prefix, String childrenPrefix) {
            buffer.append(prefix);
            buffer.append(name);
            buffer.append('\n');
            for (Iterator<Person> it = children.iterator(); it.hasNext();) {
                Person next = it.next();
                if (it.hasNext()) {
                    next.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
                } else {
                    next.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
                }
            }
        }


    }

}
