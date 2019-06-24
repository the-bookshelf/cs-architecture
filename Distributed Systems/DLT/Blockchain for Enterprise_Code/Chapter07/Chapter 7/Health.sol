pragma solidity ^0.4.22;

contract Health {

    address owner;

    struct ServiceProvider {
        string publicKey;
    }

    struct Permission {
        bool read;
        bool write;
        string reEncKey; //Re-Encrypt Key
    }

    struct Token {
        int status;
        bool read;
        bool write;
        string reEncKey; //Re-Encrypt Key
    }

    struct EMR {
        string hash;
        address issuer;
    }

    struct Patient {
        string publicKey;
        mapping (address => Permission) permissions;
        mapping (bytes32 => Token) tokens;
        bool closed;
        EMR[] EMRs;
    }

    mapping (address => ServiceProvider) serviceProviders;
    mapping (address => Patient) patients;

    event tokenVerified (bytes32 hash, address patient, address serviceProvider);
    event reEncKeyAdded (address patient, address serviceProvider);
    event patientAccountChanged(address oldAccountAddress, string oldAccountPublicKey, address newAccountAddress, string newAccountPublicKey, string reEncKey);
    event emrAdded(address patient, address serviceProvider, string emrHash);

    constructor() {
        owner = msg.sender;
    }

    //Utilities
    function fromHexChar(uint c) public pure returns (uint) {
        if (byte(c) >= byte('0') && byte(c) <= byte('9')) {
            return c - uint(byte('0'));
        }
        if (byte(c) >= byte('a') && byte(c) <= byte('f')) {
            return 10 + c - uint(byte('a'));
        }
        if (byte(c) >= byte('A') && byte(c) <= byte('F')) {
            return 10 + c - uint(byte('A'));
        }
    }

    function fromHex(string s) public pure returns (bytes) {
        bytes memory ss = bytes(s);
        require(ss.length%2 == 0); // length must be even
        bytes memory r = new bytes(ss.length/2);
        for (uint i=0; i<ss.length/2; ++i) {
            r[i] = byte(fromHexChar(uint(ss[2*i])) * 16 +
                        fromHexChar(uint(ss[2*i+1])));
        }
        return r;
    }

    //Register Patient
    function addPatient(string publicKey) returns (int reason) {
        if(address(keccak256(fromHex(publicKey))) == msg.sender) {
            patients[msg.sender].publicKey = publicKey;
        }
    }

    //Register Service provider
    function addServiceProvider(string publicKey) {
        if(address(keccak256(fromHex(publicKey))) == msg.sender) {
            serviceProviders[msg.sender].publicKey = publicKey;
        }
    }

    //Patient:
    //In QRCode include token string, address and private key
    //Adds the hash of token and derivation key in Blockchain
    function addToken(bytes32 hash, bool read, bool write, string reEncKey) {
        if(patients[msg.sender].tokens[hash].status == 0 && patients[msg.sender].closed == false) {
            patients[msg.sender].tokens[hash].status = 1;
            patients[msg.sender].tokens[hash].read = read;
            patients[msg.sender].tokens[hash].write = write;
            patients[msg.sender].tokens[hash].reEncKey = reEncKey;
        }
    }

    //Service Provider proves the token to get access
    function requestAccess(string token, address patient) {
        bytes32 hash = sha256(token);
        if(patients[patient].tokens[hash].status == 1) {
            patients[patient].tokens[hash].status = 2;
            patients[patient].permissions[msg.sender].read = patients[patient].tokens[hash].read;
            patients[patient].permissions[msg.sender].write = patients[patient].tokens[hash].write;
            patients[patient].permissions[msg.sender].reEncKey = patients[patient].tokens[hash].reEncKey;
            tokenVerified(hash, patient, msg.sender);
        }
    }

    //Add EMR
    function addEMR(address patient, string hash) {
        if(patients[patient].permissions[msg.sender].write == true) {
            patients[patient].EMRs.push(EMR(hash, msg.sender));
            emrAdded(patient, msg.sender, hash);
        }
    }

    function getPatientPublicKey(address patient) returns (string publicKey) {
        return patients[patient].publicKey;
    }

    function isPatientProfileClosed(address patient) returns (bool isClosed) {
        return patients[patient].closed;
    }

    function getServiceProviderPublicKey(address serviceProvider) returns (string publicKey) {
        return serviceProviders[serviceProvider].publicKey;
    }

    //Revoke Access. Here you aren't changing the key.
    function revokeServiceProviderAccess(address serviceProvider) {
        patients[msg.sender].permissions[serviceProvider].read = false;
        patients[msg.sender].permissions[serviceProvider].write = false;
    }

    function getPermission(address patient, address serviceProvider) returns(bool read, bool write, string reEncKey) {
        return (patients[patient].permissions[serviceProvider].read, patients[patient].permissions[serviceProvider].read, patients[patient].permissions[serviceProvider].reEncKey);
    }

    function getToken(address patient, bytes32 hash) returns (int status, bool read, bool write, string reEncKey) {
        return (patients[patient].tokens[hash].status, patients[patient].tokens[hash].read, patients[patient].tokens[hash].write, patients[patient].tokens[hash].reEncKey);
    }

    //Change your keys to revoke old account and move EMRs to new account.
    function changePatientAccount(string reEncKey, address newAddress, string newPublicKey) {
        patients[msg.sender].closed = true;
        if(address(keccak256(fromHex(newPublicKey))) == newAddress) {
            patients[newAddress].publicKey = newPublicKey;
            patientAccountChanged(msg.sender, patients[msg.sender].publicKey, newAddress, newPublicKey, reEncKey);
        }
    }
}
