// Create a new account
web3.personal.newAccount("<password>")
// $ geth account new

// Unlock an account with passcode
personal.unlockAccount("<address>", "<password>")
// Lock an unlocked account
personal.lockAccount("<your_address>")

// Import private key
web3.personal.importRawKey("<private key>", "<password>")
// $ geth account import <path to PrivateKey file>



// {
//     "address":"4f9e3e25a8e6ddaf976dcbf0b92285b1bb139ce2",
//     "crypto":{
//        "cipher":"aes-128-ctr",
//        "ciphertext":"0b0ae5548acc4d08134f4fe...53cb8c08eb9563a76aeb082c",
//           "cipherparams":{
//              "iv":"6079a38e159b95f88e0f03fbdae0f526"
//           },
//           "kdf":"scrypt",
//           "kdfparams {
//              "dklen":32,
//              "n":262144,
//              "p":1,
//              "r":8,
//              "salt":"7ed09d37d80048c21490fc9...dc10b3cef10e544017"
//            },
//        "mac":"0d8ac3314c4e9c9aaac515995c...43bf46f17d2d78bb84a6"
//     },
//     "id":"7b6630ea-8fc8-4c35-a54f-272283c8f579",
//     "version":3
// }