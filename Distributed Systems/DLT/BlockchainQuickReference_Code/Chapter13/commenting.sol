pragma solidity ^0.4.19;
/// @title A simulator for Batman, Gotham's Hero
/// @author DC-man
/// @notice You can use this contract for only the most basic simulation
/// @dev All function calls are currently implement without side effects
contract Batman {
/// @author Samanyu Chopra
/// @notice Determine if Bugs will accept `(_weapons)` to kill
/// @dev String comparison may be inefficient
/// @param _weapons The name weapons to save in the repo (English)
/// @return true if Batman will keep it, false otherwise
function doesKeep(string _weapons) external pure returns (bool) {
return keccak256(_weapons) == keccak256("Shotgun");
}
}