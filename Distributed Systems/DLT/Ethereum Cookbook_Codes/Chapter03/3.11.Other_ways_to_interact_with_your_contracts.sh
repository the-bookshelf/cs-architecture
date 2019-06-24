#### Nethereum implementation

PM > Install-Package Nethereum.Web3
# OR
PM > Install-Package Nethereum.Portable

dotnet add package Nethereum.Web3
# OR
dotnet add package Nethereum.Portable

# Will connect to default node
var web3 = new Nethereum.Web3.Web3();
# Custom node address
var web3 = new Nethereum.Web3.Web3("https://localhost:7545");
# IPC Connection
var ipcClient = new Nethereum.JsonRpc.IpcClient("./geth.ipc");
var web3 = new Nethereum.Web3.Web3(ipcClient);


#### Web3J Implementation

# Java 8
<dependency>
    <groupId>org.web3j</groupId>
    <artifactId>core</artifactId>
    <version>3.3.1</version>
</dependency>
# Android
<dependency>
    <groupId>org.web3j</groupId>
    <artifactId>core</artifactId>
    <version>3.3.1-android</version>
</dependency>

# Java 8
compile ('org.web3j:core:3.3.1')
# Android
compile ('org.web3j:core:3.3.1-android')

Web3j web3 = Web3j.build(new HttpService());

# sync
Web3ClientVersion web3ClientVersion =
web3.web3ClientVersion().send();
String clientVersion = web3ClientVersion.getWeb3ClientVersion();    
# async
Web3ClientVersion web3ClientVersion =
web3.web3ClientVersion().sendAsync().get();
String clientVersion = web3ClientVersion.getWeb3ClientVersion();