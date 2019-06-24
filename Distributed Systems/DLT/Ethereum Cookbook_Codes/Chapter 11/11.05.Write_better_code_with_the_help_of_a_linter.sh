npm install -g solium

solium -V

solium --init

# Lint a specific file
solium -f fileName.sol

# Lint files present in the folder
solium -d contracts/

solium -d contracts/ --fix

solium --watch --dir contracts/

# Avoid linting for the next line
/* solium-disable-next-line */

# Avoid the whole file
/* solium-disable */
