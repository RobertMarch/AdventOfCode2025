echo "Setting up day $1"

DAY_NUMBER=`printf "%02d" $1`
FILE_NAME="src/main/kotlin/days/Day${DAY_NUMBER}.kt"
INPUT_FILE_NAME="src/main/resources/inputs/day$DAY_NUMBER.txt"
TEST_FILE_NAME="src/test/kotlin/days/Day${DAY_NUMBER}Test.kt"

if test -f "$FILE_NAME"; then
    echo "File already exists"
    exit 1
fi

cp src/main/kotlin/days/DayXX.kt "${FILE_NAME}"
sed -i 's/99/'"$1"'/' $FILE_NAME
sed -i 's/XX/'"$DAY_NUMBER"'/g' ${FILE_NAME}

touch $INPUT_FILE_NAME

cp src/test/kotlin/days/DayXXTest.kt "${TEST_FILE_NAME}"
sed -i 's/99/'"$1"'/' $TEST_FILE_NAME
sed -i 's/XX/'"$DAY_NUMBER"'/g' ${TEST_FILE_NAME}

echo "Set up for day $1 complete"
