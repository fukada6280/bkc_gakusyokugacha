class Knapsack {

    public static void pickName( final List<String> names, final String startingLetter) {
        final Optional<String> foundName =
                names.stream()
                        .filter(name -> name.startsWith(startingLetter))
                        .findFirst();

        System.out.println(String.format("A name starting with %s: %s",
                startingLetter, foundName.orElse("No name found")));
    }
}
