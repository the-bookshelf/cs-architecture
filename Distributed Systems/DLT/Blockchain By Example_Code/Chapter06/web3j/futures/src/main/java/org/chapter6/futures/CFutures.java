package org.chapter6.futures;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.4.0.
 */
public class CFutures extends Contract {
    private static final String BINARY = "0x60806040523480156200001157600080fd5b5060405160c0806200282a83398101806040528101908080519060200190929190805190602001909291908051906020019092919080519060200190929190805190602001909291908051906020019092919050505085600860000181905550846008600101819055508360086002018190555081600860030160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555081600760006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555082600660006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555082600860040160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600860050181905550620001b2620001be640100000000026401000000009004565b50505050505062000f2f565b7f096835e36c2ccea88ff2b3aca87dfc938b977e52ea656873ff76a8dba50d4d346040518080602001828103825260358152602001807f4f7261636c697a65207175657279207761732073656e742c207374616e64696e81526020017f6720627920666f722074686520616e737765722e2e000000000000000000000081525060400191505060405180910390a16200031e6040805190810160405280600381526020017f55524c0000000000000000000000000000000000000000000000000000000000815250608060405190810160405280604581526020017f786d6c2868747470733a2f2f7777772e6675656c65636f6e6f6d792e676f762f81526020017f77732f726573742f6675656c707269636573292e6675656c5072696365732e6481526020017f696573656c00000000000000000000000000000000000000000000000000000081525062000321640100000000026401000000009004565b50565b60008060008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161480620003a557506000620003a36000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff16620008ff640100000000026401000000009004565b145b15620003c857620003c660006200090a640100000000026401000000009004565b505b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166338cc48316040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1580156200044e57600080fd5b505af115801562000463573d6000803e3d6000fd5b505050506040513d60208110156200047a57600080fd5b810190808051906020019092919050505073ffffffffffffffffffffffffffffffffffffffff16600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515620005e6576000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166338cc48316040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1580156200056857600080fd5b505af11580156200057d573d6000803e3d6000fd5b505050506040513d60208110156200059457600080fd5b8101908080519060200190929190505050600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663524f3889856040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825283818151815260200191508051906020019080838360005b838110156200069257808201518184015260208101905062000675565b50505050905090810190601f168015620006c05780820380516001836020036101000a031916815260200191505b5092505050602060405180830381600087803b158015620006e057600080fd5b505af1158015620006f5573d6000803e3d6000fd5b505050506040513d60208110156200070c57600080fd5b8101908080519060200190929190505050905062030d403a02670de0b6b3a764000001811115620007445760006001029150620008f8565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663adf59f9982600087876040518563ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b83811015620007fe578082015181840152602081019050620007e1565b50505050905090810190601f1680156200082c5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b83811015620008675780820151818401526020810190506200084a565b50505050905090810190601f168015620008955780820380516001836020036101000a031916815260200191505b50955050505050506020604051808303818588803b158015620008b757600080fd5b505af1158015620008cc573d6000803e3d6000fd5b50505050506040513d6020811015620008e457600080fd5b810190808051906020019092919050505091505b5092915050565b6000813b9050919050565b6000806200093b731d3b2638a7cc9f2cb3d298a3da7a90b67e5506ed620008ff640100000000026401000000009004565b1115620009ef57731d3b2638a7cc9f2cb3d298a3da7a90b67e5506ed6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550620009e56040805190810160405280600b81526020017f6574685f6d61696e6e657400000000000000000000000000000000000000000081525062000e64640100000000026401000000009004565b6001905062000e5f565b600062000a1f73c03a2615d5efaf5f49f60b7bb6583eaec212fdf1620008ff640100000000026401000000009004565b111562000ad35773c03a2615d5efaf5f49f60b7bb6583eaec212fdf16000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555062000ac96040805190810160405280600c81526020017f6574685f726f707374656e33000000000000000000000000000000000000000081525062000e64640100000000026401000000009004565b6001905062000e5f565b600062000b0373b7a07bcf2ba2f2703b24c0691b5278999c59ac7e620008ff640100000000026401000000009004565b111562000bb75773b7a07bcf2ba2f2703b24c0691b5278999c59ac7e6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555062000bad6040805190810160405280600981526020017f6574685f6b6f76616e000000000000000000000000000000000000000000000081525062000e64640100000000026401000000009004565b6001905062000e5f565b600062000be773146500cfd35b22e4a392fe0adc06de1a1368ed48620008ff640100000000026401000000009004565b111562000c9b5773146500cfd35b22e4a392fe0adc06de1a1368ed486000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555062000c916040805190810160405280600b81526020017f6574685f72696e6b65627900000000000000000000000000000000000000000081525062000e64640100000000026401000000009004565b6001905062000e5f565b600062000ccb736f485c8bf6fc43ea212e93bbf8ce046c7f1cb475620008ff640100000000026401000000009004565b111562000d3057736f485c8bf6fc43ea212e93bbf8ce046c7f1cb4756000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506001905062000e5f565b600062000d607320e12a1f859b3feae5fb2a0a32c18f5a65555bbf620008ff640100000000026401000000009004565b111562000dc5577320e12a1f859b3feae5fb2a0a32c18f5a65555bbf6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506001905062000e5f565b600062000df57351efaf4c8b3c9afbd5ab9f4bbc82784ab6ef8faa620008ff640100000000026401000000009004565b111562000e5a577351efaf4c8b3c9afbd5ab9f4bbc82784ab6ef8faa6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506001905062000e5f565b600090505b919050565b806002908051906020019062000e7c92919062000e80565b5050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1062000ec357805160ff191683800117855562000ef4565b8280016001018555821562000ef4579182015b8281111562000ef357825182559160200191906001019062000ed6565b5b50905062000f03919062000f07565b5090565b62000f2c91905b8082111562000f2857600081600090555060010162000f0e565b5090565b90565b6118eb8062000f3f6000396000f300608060405260043610610099576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806323bf2bfa1461009e57806327dc297e146100c957806338bbfa50146101405780636950ae62146101fd578063845873b414610258578063bd95ad0a14610262578063cfda2d4614610291578063d0e30db0146102bc578063d5556544146102de575b600080fd5b3480156100aa57600080fd5b506100b3610309565b6040518082815260200191505060405180910390f35b3480156100d557600080fd5b5061013e6004803603810190808035600019169060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610313565b005b34801561014c57600080fd5b506101fb6004803603810190808035600019169060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610405565b005b34801561020957600080fd5b5061023e600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061040a565b604051808215151515815260200191505060405180910390f35b6102606104b2565b005b34801561026e57600080fd5b50610277610604565b604051808215151515815260200191505060405180910390f35b34801561029d57600080fd5b506102a66106c4565b6040518082815260200191505060405180910390f35b6102c46106ca565b604051808215151515815260200191505060405180910390f35b3480156102ea57600080fd5b506102f3610758565b6040518082815260200191505060405180910390f35b6000600e54905090565b61031b610770565b73ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561035457600080fd5b7fc304083a35fe753587063bd4486ba9a62b6e4a539f328e88895d44323e0f99f4816040518080602001828103825283818151815260200191508051906020019080838360005b838110156103b657808201518184015260208101905061039b565b50505050905090810190601f1680156103e35780820380516001836020036101000a031916815260200191505b509250505060405180910390a16103fb816002610ace565b600e819055505050565b505050565b6000600660009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561046857600080fd5b81600660006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060019050919050565b7f096835e36c2ccea88ff2b3aca87dfc938b977e52ea656873ff76a8dba50d4d346040518080602001828103825260358152602001807f4f7261636c697a65207175657279207761732073656e742c207374616e64696e81526020017f6720627920666f722074686520616e737765722e2e000000000000000000000081525060400191505060405180910390a16106016040805190810160405280600381526020017f55524c0000000000000000000000000000000000000000000000000000000000815250608060405190810160405280604581526020017f786d6c2868747470733a2f2f7777772e6675656c65636f6e6f6d792e676f762f81526020017f77732f726573742f6675656c707269636573292e6675656c5072696365732e6481526020017f696573656c000000000000000000000000000000000000000000000000000000815250610dc2565b50565b60006008600501544210158015610632575060003073ffffffffffffffffffffffffffffffffffffffff1631115b151561063d57600080fd5b600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc3073ffffffffffffffffffffffffffffffffffffffff16319081150290604051600060405180830381858888f193505050501580156106bc573d6000803e3d6000fd5b506001905090565b600e5481565b600060086002015460086001015402341415156106e657600080fd5b7f81e0a0a9401f56109ea65cc9c8540f79ba753fd1fafe94aa353bb8d01b9980e63433604051808381526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019250505060405180910390a16001905090565b6000600e546008600201540360086001015402905090565b6000806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614806107e0575060006107de6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff16611364565b145b156107f1576107ef600061136f565b505b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166338cc48316040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561087657600080fd5b505af115801561088a573d6000803e3d6000fd5b505050506040513d60208110156108a057600080fd5b810190808051906020019092919050505073ffffffffffffffffffffffffffffffffffffffff16600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515610a08576000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166338cc48316040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561098c57600080fd5b505af11580156109a0573d6000803e3d6000fd5b505050506040513d60208110156109b657600080fd5b8101908080519060200190929190505050600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c281d19e6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b158015610a8e57600080fd5b505af1158015610aa2573d6000803e3d6000fd5b505050506040513d6020811015610ab857600080fd5b8101908080519060200190929190505050905090565b6000606060008060008693506000925060009150600090505b8351811015610da35760307f0100000000000000000000000000000000000000000000000000000000000000028482815181101515610b2257fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000027effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff191610158015610c3a575060397f0100000000000000000000000000000000000000000000000000000000000000028482815181101515610bca57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000027effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff191611155b15610ceb578115610c5d576000861415610c5357610da3565b8580600190039650505b600a8302925060308482815181101515610c7357fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000027f010000000000000000000000000000000000000000000000000000000000000090040383019250610d96565b602e7f0100000000000000000000000000000000000000000000000000000000000000028482815181101515610d1d57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000027effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff19161415610d9557600191505b5b8080600101915050610ae7565b6000861115610db55785600a0a830292505b8294505050505092915050565b60008060008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161480610e3457506000610e326000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff16611364565b145b15610e4557610e43600061136f565b505b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166338cc48316040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b158015610eca57600080fd5b505af1158015610ede573d6000803e3d6000fd5b505050506040513d6020811015610ef457600080fd5b810190808051906020019092919050505073ffffffffffffffffffffffffffffffffffffffff16600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614151561105c576000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166338cc48316040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b158015610fe057600080fd5b505af1158015610ff4573d6000803e3d6000fd5b505050506040513d602081101561100a57600080fd5b8101908080519060200190929190505050600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663524f3889856040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825283818151815260200191508051906020019080838360005b838110156111065780820151818401526020810190506110eb565b50505050905090810190601f1680156111335780820380516001836020036101000a031916815260200191505b5092505050602060405180830381600087803b15801561115257600080fd5b505af1158015611166573d6000803e3d6000fd5b505050506040513d602081101561117c57600080fd5b8101908080519060200190929190505050905062030d403a02670de0b6b3a7640000018111156111b2576000600102915061135d565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663adf59f9982600087876040518563ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b8381101561126a57808201518184015260208101905061124f565b50505050905090810190601f1680156112975780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b838110156112d05780820151818401526020810190506112b5565b50505050905090810190601f1680156112fd5780820380516001836020036101000a031916815260200191505b50955050505050506020604051808303818588803b15801561131e57600080fd5b505af1158015611332573d6000803e3d6000fd5b50505050506040513d602081101561134957600080fd5b810190808051906020019092919050505091505b5092915050565b6000813b9050919050565b60008061138f731d3b2638a7cc9f2cb3d298a3da7a90b67e5506ed611364565b111561143057731d3b2638a7cc9f2cb3d298a3da7a90b67e5506ed6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506114276040805190810160405280600b81526020017f6574685f6d61696e6e6574000000000000000000000000000000000000000000815250611800565b600190506117fb565b600061144f73c03a2615d5efaf5f49f60b7bb6583eaec212fdf1611364565b11156114f05773c03a2615d5efaf5f49f60b7bb6583eaec212fdf16000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506114e76040805190810160405280600c81526020017f6574685f726f707374656e330000000000000000000000000000000000000000815250611800565b600190506117fb565b600061150f73b7a07bcf2ba2f2703b24c0691b5278999c59ac7e611364565b11156115b05773b7a07bcf2ba2f2703b24c0691b5278999c59ac7e6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506115a76040805190810160405280600981526020017f6574685f6b6f76616e0000000000000000000000000000000000000000000000815250611800565b600190506117fb565b60006115cf73146500cfd35b22e4a392fe0adc06de1a1368ed48611364565b11156116705773146500cfd35b22e4a392fe0adc06de1a1368ed486000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506116676040805190810160405280600b81526020017f6574685f72696e6b656279000000000000000000000000000000000000000000815250611800565b600190506117fb565b600061168f736f485c8bf6fc43ea212e93bbf8ce046c7f1cb475611364565b11156116f257736f485c8bf6fc43ea212e93bbf8ce046c7f1cb4756000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600190506117fb565b60006117117320e12a1f859b3feae5fb2a0a32c18f5a65555bbf611364565b1115611774577320e12a1f859b3feae5fb2a0a32c18f5a65555bbf6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600190506117fb565b60006117937351efaf4c8b3c9afbd5ab9f4bbc82784ab6ef8faa611364565b11156117f6577351efaf4c8b3c9afbd5ab9f4bbc82784ab6ef8faa6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600190506117fb565b600090505b919050565b806002908051906020019061181692919061181a565b5050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061185b57805160ff1916838001178555611889565b82800160010185558215611889579182015b8281111561188857825182559160200191906001019061186d565b5b509050611896919061189a565b5090565b6118bc91905b808211156118b85760008160009055506001016118a0565b5090565b905600a165627a7a723058207df168e8e0f07ba1fbc547dae5ab4fb2fe306e43164b8773412ccdeb19acef510029";

    public static final String FUNC_FUELPRICEUSD = "fuelPriceUSD";

    public static final String FUNC___CALLBACK = "__callback";

    public static final String FUNC_UPDATEPRICE = "updateprice";

    public static final String FUNC_DEPOSIT = "deposit";

    public static final String FUNC_SELLCONTRACT = "sellcontract";

    public static final String FUNC_GETPAID = "getpaid";

    public static final String FUNC_OFFSET = "offset";

    public static final String FUNC_GETFUELPRICEUSD = "getfuelPriceUSD";

    public static final Event DEPOSITEV_EVENT = new Event("DepositEv", 
            Arrays.<TypeReference<?>>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event NEWORACLIZEQUERY_EVENT = new Event("NewOraclizeQuery", 
            Arrays.<TypeReference<?>>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event NEWDIESELPRICE_EVENT = new Event("NewDieselPrice", 
            Arrays.<TypeReference<?>>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
    }

    protected CFutures(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected CFutures(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<BigInteger> fuelPriceUSD() {
        final Function function = new Function(FUNC_FUELPRICEUSD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static RemoteCall<CFutures> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger assetID, BigInteger Quantity, BigInteger price, String buyer, String seller, BigInteger date) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(assetID), 
                new org.web3j.abi.datatypes.generated.Uint256(Quantity), 
                new org.web3j.abi.datatypes.generated.Uint256(price), 
                new org.web3j.abi.datatypes.Address(buyer), 
                new org.web3j.abi.datatypes.Address(seller), 
                new org.web3j.abi.datatypes.generated.Uint256(date)));
        return deployRemoteCall(CFutures.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<CFutures> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger assetID, BigInteger Quantity, BigInteger price, String buyer, String seller, BigInteger date) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(assetID), 
                new org.web3j.abi.datatypes.generated.Uint256(Quantity), 
                new org.web3j.abi.datatypes.generated.Uint256(price), 
                new org.web3j.abi.datatypes.Address(buyer), 
                new org.web3j.abi.datatypes.Address(seller), 
                new org.web3j.abi.datatypes.generated.Uint256(date)));
        return deployRemoteCall(CFutures.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public List<DepositEvEventResponse> getDepositEvEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DEPOSITEV_EVENT, transactionReceipt);
        ArrayList<DepositEvEventResponse> responses = new ArrayList<DepositEvEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DepositEvEventResponse typedResponse = new DepositEvEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.sender = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<DepositEvEventResponse> depositEvEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, DepositEvEventResponse>() {
            @Override
            public DepositEvEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DEPOSITEV_EVENT, log);
                DepositEvEventResponse typedResponse = new DepositEvEventResponse();
                typedResponse.log = log;
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.sender = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<DepositEvEventResponse> depositEvEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DEPOSITEV_EVENT));
        return depositEvEventObservable(filter);
    }

    public List<NewOraclizeQueryEventResponse> getNewOraclizeQueryEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NEWORACLIZEQUERY_EVENT, transactionReceipt);
        ArrayList<NewOraclizeQueryEventResponse> responses = new ArrayList<NewOraclizeQueryEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NewOraclizeQueryEventResponse typedResponse = new NewOraclizeQueryEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.description = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<NewOraclizeQueryEventResponse> newOraclizeQueryEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, NewOraclizeQueryEventResponse>() {
            @Override
            public NewOraclizeQueryEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NEWORACLIZEQUERY_EVENT, log);
                NewOraclizeQueryEventResponse typedResponse = new NewOraclizeQueryEventResponse();
                typedResponse.log = log;
                typedResponse.description = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<NewOraclizeQueryEventResponse> newOraclizeQueryEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWORACLIZEQUERY_EVENT));
        return newOraclizeQueryEventObservable(filter);
    }

    public List<NewDieselPriceEventResponse> getNewDieselPriceEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NEWDIESELPRICE_EVENT, transactionReceipt);
        ArrayList<NewDieselPriceEventResponse> responses = new ArrayList<NewDieselPriceEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NewDieselPriceEventResponse typedResponse = new NewDieselPriceEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.price = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<NewDieselPriceEventResponse> newDieselPriceEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, NewDieselPriceEventResponse>() {
            @Override
            public NewDieselPriceEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NEWDIESELPRICE_EVENT, log);
                NewDieselPriceEventResponse typedResponse = new NewDieselPriceEventResponse();
                typedResponse.log = log;
                typedResponse.price = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<NewDieselPriceEventResponse> newDieselPriceEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWDIESELPRICE_EVENT));
        return newDieselPriceEventObservable(filter);
    }

    public RemoteCall<TransactionReceipt> __callback(byte[] myid, String result) {
        final Function function = new Function(
                FUNC___CALLBACK, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(myid), 
                new org.web3j.abi.datatypes.Utf8String(result)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> __callback(byte[] myid, String result, byte[] proof) {
        final Function function = new Function(
                FUNC___CALLBACK, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(myid), 
                new org.web3j.abi.datatypes.Utf8String(result), 
                new org.web3j.abi.datatypes.DynamicBytes(proof)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> updateprice(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_UPDATEPRICE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> deposit(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_DEPOSIT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> sellcontract(String newhedger) {
        final Function function = new Function(
                FUNC_SELLCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(newhedger)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> getpaid() {
        final Function function = new Function(
                FUNC_GETPAID, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> offset() {
        final Function function = new Function(FUNC_OFFSET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getfuelPriceUSD() {
        final Function function = new Function(FUNC_GETFUELPRICEUSD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static CFutures load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new CFutures(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static CFutures load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new CFutures(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class DepositEvEventResponse {
        public Log log;

        public BigInteger amount;

        public String sender;
    }

    public static class NewOraclizeQueryEventResponse {
        public Log log;

        public String description;
    }

    public static class NewDieselPriceEventResponse {
        public Log log;

        public String price;
    }
}
