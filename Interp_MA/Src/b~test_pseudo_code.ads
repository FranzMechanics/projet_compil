pragma Ada_95;
with System;
package ada_main is
   pragma Warnings (Off);

   gnat_argc : Integer;
   gnat_argv : System.Address;
   gnat_envp : System.Address;

   pragma Import (C, gnat_argc);
   pragma Import (C, gnat_argv);
   pragma Import (C, gnat_envp);

   gnat_exit_status : Integer;
   pragma Import (C, gnat_exit_status);

   GNAT_Version : constant String :=
                    "GNAT Version: 4.9.3" & ASCII.NUL;
   pragma Export (C, GNAT_Version, "__gnat_version");

   Ada_Main_Program_Name : constant String := "_ada_test_pseudo_code" & ASCII.NUL;
   pragma Export (C, Ada_Main_Program_Name, "__gnat_ada_main_program_name");

   procedure adainit;
   pragma Export (C, adainit, "adainit");

   procedure adafinal;
   pragma Export (C, adafinal, "adafinal");

   function main
     (argc : Integer;
      argv : System.Address;
      envp : System.Address)
      return Integer;
   pragma Export (C, main, "main");

   type Version_32 is mod 2 ** 32;
   u00001 : constant Version_32 := 16#870f75ca#;
   pragma Export (C, u00001, "test_pseudo_codeB");
   u00002 : constant Version_32 := 16#a964624f#;
   pragma Export (C, u00002, "system__standard_libraryB");
   u00003 : constant Version_32 := 16#c15e0628#;
   pragma Export (C, u00003, "system__standard_libraryS");
   u00004 : constant Version_32 := 16#3ffc8e18#;
   pragma Export (C, u00004, "adaS");
   u00005 : constant Version_32 := 16#9a3f0a9b#;
   pragma Export (C, u00005, "ada__text_ioB");
   u00006 : constant Version_32 := 16#27578d68#;
   pragma Export (C, u00006, "ada__text_ioS");
   u00007 : constant Version_32 := 16#e9502879#;
   pragma Export (C, u00007, "ada__exceptionsB");
   u00008 : constant Version_32 := 16#e1be92cd#;
   pragma Export (C, u00008, "ada__exceptionsS");
   u00009 : constant Version_32 := 16#51ba2b93#;
   pragma Export (C, u00009, "ada__exceptions__last_chance_handlerB");
   u00010 : constant Version_32 := 16#909606f8#;
   pragma Export (C, u00010, "ada__exceptions__last_chance_handlerS");
   u00011 : constant Version_32 := 16#90249111#;
   pragma Export (C, u00011, "systemS");
   u00012 : constant Version_32 := 16#77a147eb#;
   pragma Export (C, u00012, "system__soft_linksB");
   u00013 : constant Version_32 := 16#6144caac#;
   pragma Export (C, u00013, "system__soft_linksS");
   u00014 : constant Version_32 := 16#65bb1402#;
   pragma Export (C, u00014, "system__parametersB");
   u00015 : constant Version_32 := 16#3ba8257b#;
   pragma Export (C, u00015, "system__parametersS");
   u00016 : constant Version_32 := 16#643ddf46#;
   pragma Export (C, u00016, "system__secondary_stackB");
   u00017 : constant Version_32 := 16#3b455e78#;
   pragma Export (C, u00017, "system__secondary_stackS");
   u00018 : constant Version_32 := 16#39a03df9#;
   pragma Export (C, u00018, "system__storage_elementsB");
   u00019 : constant Version_32 := 16#bde7db15#;
   pragma Export (C, u00019, "system__storage_elementsS");
   u00020 : constant Version_32 := 16#41837d1e#;
   pragma Export (C, u00020, "system__stack_checkingB");
   u00021 : constant Version_32 := 16#1e9bfaf9#;
   pragma Export (C, u00021, "system__stack_checkingS");
   u00022 : constant Version_32 := 16#7ff7f3a3#;
   pragma Export (C, u00022, "system__exception_tableB");
   u00023 : constant Version_32 := 16#6ca6d92c#;
   pragma Export (C, u00023, "system__exception_tableS");
   u00024 : constant Version_32 := 16#c3335bfd#;
   pragma Export (C, u00024, "system__htableB");
   u00025 : constant Version_32 := 16#14e622fb#;
   pragma Export (C, u00025, "system__htableS");
   u00026 : constant Version_32 := 16#089f5cd0#;
   pragma Export (C, u00026, "system__string_hashB");
   u00027 : constant Version_32 := 16#b6b84985#;
   pragma Export (C, u00027, "system__string_hashS");
   u00028 : constant Version_32 := 16#9e373b74#;
   pragma Export (C, u00028, "system__exceptionsB");
   u00029 : constant Version_32 := 16#a83a37b3#;
   pragma Export (C, u00029, "system__exceptionsS");
   u00030 : constant Version_32 := 16#cd9019cf#;
   pragma Export (C, u00030, "system__exceptions__machineS");
   u00031 : constant Version_32 := 16#a2eb6533#;
   pragma Export (C, u00031, "system__exceptions_debugB");
   u00032 : constant Version_32 := 16#6b2380a9#;
   pragma Export (C, u00032, "system__exceptions_debugS");
   u00033 : constant Version_32 := 16#570325c8#;
   pragma Export (C, u00033, "system__img_intB");
   u00034 : constant Version_32 := 16#92ff71d3#;
   pragma Export (C, u00034, "system__img_intS");
   u00035 : constant Version_32 := 16#ff5c7695#;
   pragma Export (C, u00035, "system__tracebackB");
   u00036 : constant Version_32 := 16#b8200e4c#;
   pragma Export (C, u00036, "system__tracebackS");
   u00037 : constant Version_32 := 16#8c33a517#;
   pragma Export (C, u00037, "system__wch_conB");
   u00038 : constant Version_32 := 16#8b59b3c3#;
   pragma Export (C, u00038, "system__wch_conS");
   u00039 : constant Version_32 := 16#9721e840#;
   pragma Export (C, u00039, "system__wch_stwB");
   u00040 : constant Version_32 := 16#a6489fc2#;
   pragma Export (C, u00040, "system__wch_stwS");
   u00041 : constant Version_32 := 16#9b29844d#;
   pragma Export (C, u00041, "system__wch_cnvB");
   u00042 : constant Version_32 := 16#84ee0930#;
   pragma Export (C, u00042, "system__wch_cnvS");
   u00043 : constant Version_32 := 16#69adb1b9#;
   pragma Export (C, u00043, "interfacesS");
   u00044 : constant Version_32 := 16#ece6fdb6#;
   pragma Export (C, u00044, "system__wch_jisB");
   u00045 : constant Version_32 := 16#049e1011#;
   pragma Export (C, u00045, "system__wch_jisS");
   u00046 : constant Version_32 := 16#8cb17bcd#;
   pragma Export (C, u00046, "system__traceback_entriesB");
   u00047 : constant Version_32 := 16#2535f183#;
   pragma Export (C, u00047, "system__traceback_entriesS");
   u00048 : constant Version_32 := 16#1b5643e2#;
   pragma Export (C, u00048, "ada__streamsB");
   u00049 : constant Version_32 := 16#2564c958#;
   pragma Export (C, u00049, "ada__streamsS");
   u00050 : constant Version_32 := 16#db5c917c#;
   pragma Export (C, u00050, "ada__io_exceptionsS");
   u00051 : constant Version_32 := 16#08ba48f3#;
   pragma Export (C, u00051, "ada__tagsB");
   u00052 : constant Version_32 := 16#ee1e5cdd#;
   pragma Export (C, u00052, "ada__tagsS");
   u00053 : constant Version_32 := 16#c12203be#;
   pragma Export (C, u00053, "system__unsigned_typesS");
   u00054 : constant Version_32 := 16#1e25d3f1#;
   pragma Export (C, u00054, "system__val_lluB");
   u00055 : constant Version_32 := 16#bbd054cc#;
   pragma Export (C, u00055, "system__val_lluS");
   u00056 : constant Version_32 := 16#27b600b2#;
   pragma Export (C, u00056, "system__val_utilB");
   u00057 : constant Version_32 := 16#3c8427ef#;
   pragma Export (C, u00057, "system__val_utilS");
   u00058 : constant Version_32 := 16#d1060688#;
   pragma Export (C, u00058, "system__case_utilB");
   u00059 : constant Version_32 := 16#b42df8c6#;
   pragma Export (C, u00059, "system__case_utilS");
   u00060 : constant Version_32 := 16#e0b7a7e8#;
   pragma Export (C, u00060, "interfaces__c_streamsB");
   u00061 : constant Version_32 := 16#6c8b8ac5#;
   pragma Export (C, u00061, "interfaces__c_streamsS");
   u00062 : constant Version_32 := 16#bfa64d6c#;
   pragma Export (C, u00062, "system__crtlS");
   u00063 : constant Version_32 := 16#ec6e1273#;
   pragma Export (C, u00063, "system__file_ioB");
   u00064 : constant Version_32 := 16#0165f036#;
   pragma Export (C, u00064, "system__file_ioS");
   u00065 : constant Version_32 := 16#8cbe6205#;
   pragma Export (C, u00065, "ada__finalizationB");
   u00066 : constant Version_32 := 16#22e22193#;
   pragma Export (C, u00066, "ada__finalizationS");
   u00067 : constant Version_32 := 16#95817ed8#;
   pragma Export (C, u00067, "system__finalization_rootB");
   u00068 : constant Version_32 := 16#dfd6e281#;
   pragma Export (C, u00068, "system__finalization_rootS");
   u00069 : constant Version_32 := 16#769e25e6#;
   pragma Export (C, u00069, "interfaces__cB");
   u00070 : constant Version_32 := 16#96001448#;
   pragma Export (C, u00070, "interfaces__cS");
   u00071 : constant Version_32 := 16#d0432c8d#;
   pragma Export (C, u00071, "system__img_enum_newB");
   u00072 : constant Version_32 := 16#f16897d1#;
   pragma Export (C, u00072, "system__img_enum_newS");
   u00073 : constant Version_32 := 16#eec8f81b#;
   pragma Export (C, u00073, "system__os_libB");
   u00074 : constant Version_32 := 16#fc501b71#;
   pragma Export (C, u00074, "system__os_libS");
   u00075 : constant Version_32 := 16#1a817b8e#;
   pragma Export (C, u00075, "system__stringsB");
   u00076 : constant Version_32 := 16#ee9b8077#;
   pragma Export (C, u00076, "system__stringsS");
   u00077 : constant Version_32 := 16#da3df06e#;
   pragma Export (C, u00077, "system__file_control_blockS");
   u00078 : constant Version_32 := 16#1f8826cb#;
   pragma Export (C, u00078, "system__finalization_mastersB");
   u00079 : constant Version_32 := 16#df27fd08#;
   pragma Export (C, u00079, "system__finalization_mastersS");
   u00080 : constant Version_32 := 16#57a37a42#;
   pragma Export (C, u00080, "system__address_imageB");
   u00081 : constant Version_32 := 16#31c80c2b#;
   pragma Export (C, u00081, "system__address_imageS");
   u00082 : constant Version_32 := 16#7268f812#;
   pragma Export (C, u00082, "system__img_boolB");
   u00083 : constant Version_32 := 16#65fde0fa#;
   pragma Export (C, u00083, "system__img_boolS");
   u00084 : constant Version_32 := 16#d7aac20c#;
   pragma Export (C, u00084, "system__ioB");
   u00085 : constant Version_32 := 16#0e66665e#;
   pragma Export (C, u00085, "system__ioS");
   u00086 : constant Version_32 := 16#6d4d969a#;
   pragma Export (C, u00086, "system__storage_poolsB");
   u00087 : constant Version_32 := 16#5e6a53cc#;
   pragma Export (C, u00087, "system__storage_poolsS");
   u00088 : constant Version_32 := 16#e34550ca#;
   pragma Export (C, u00088, "system__pool_globalB");
   u00089 : constant Version_32 := 16#c88d2d16#;
   pragma Export (C, u00089, "system__pool_globalS");
   u00090 : constant Version_32 := 16#6810466c#;
   pragma Export (C, u00090, "system__memoryB");
   u00091 : constant Version_32 := 16#c959f725#;
   pragma Export (C, u00091, "system__memoryS");
   u00092 : constant Version_32 := 16#1220f12d#;
   pragma Export (C, u00092, "system__storage_pools__subpoolsB");
   u00093 : constant Version_32 := 16#b0e8cddc#;
   pragma Export (C, u00093, "system__storage_pools__subpoolsS");
   u00094 : constant Version_32 := 16#aba9f469#;
   pragma Export (C, u00094, "system__storage_pools__subpools__finalizationB");
   u00095 : constant Version_32 := 16#9662cb63#;
   pragma Export (C, u00095, "system__storage_pools__subpools__finalizationS");
   u00096 : constant Version_32 := 16#956644e6#;
   pragma Export (C, u00096, "pseudo_codeB");
   u00097 : constant Version_32 := 16#6837ff4f#;
   pragma Export (C, u00097, "pseudo_codeS");
   u00098 : constant Version_32 := 16#1384aef2#;
   pragma Export (C, u00098, "entier_esB");
   u00099 : constant Version_32 := 16#6f99cae8#;
   pragma Export (C, u00099, "entier_esS");
   u00100 : constant Version_32 := 16#f6fdca1c#;
   pragma Export (C, u00100, "ada__text_io__integer_auxB");
   u00101 : constant Version_32 := 16#b9793d30#;
   pragma Export (C, u00101, "ada__text_io__integer_auxS");
   u00102 : constant Version_32 := 16#cd6ba629#;
   pragma Export (C, u00102, "ada__text_io__generic_auxB");
   u00103 : constant Version_32 := 16#a6c327d3#;
   pragma Export (C, u00103, "ada__text_io__generic_auxS");
   u00104 : constant Version_32 := 16#d48b4eeb#;
   pragma Export (C, u00104, "system__img_biuB");
   u00105 : constant Version_32 := 16#07008bf3#;
   pragma Export (C, u00105, "system__img_biuS");
   u00106 : constant Version_32 := 16#2b864520#;
   pragma Export (C, u00106, "system__img_llbB");
   u00107 : constant Version_32 := 16#46c79b0d#;
   pragma Export (C, u00107, "system__img_llbS");
   u00108 : constant Version_32 := 16#9777733a#;
   pragma Export (C, u00108, "system__img_lliB");
   u00109 : constant Version_32 := 16#816bc4c0#;
   pragma Export (C, u00109, "system__img_lliS");
   u00110 : constant Version_32 := 16#c2d63ebb#;
   pragma Export (C, u00110, "system__img_llwB");
   u00111 : constant Version_32 := 16#efabb89b#;
   pragma Export (C, u00111, "system__img_llwS");
   u00112 : constant Version_32 := 16#8ed53197#;
   pragma Export (C, u00112, "system__img_wiuB");
   u00113 : constant Version_32 := 16#69410c61#;
   pragma Export (C, u00113, "system__img_wiuS");
   u00114 : constant Version_32 := 16#f8f38c17#;
   pragma Export (C, u00114, "system__val_intB");
   u00115 : constant Version_32 := 16#d881bb2e#;
   pragma Export (C, u00115, "system__val_intS");
   u00116 : constant Version_32 := 16#4266b2a8#;
   pragma Export (C, u00116, "system__val_unsB");
   u00117 : constant Version_32 := 16#d18aee85#;
   pragma Export (C, u00117, "system__val_unsS");
   u00118 : constant Version_32 := 16#e892b88e#;
   pragma Export (C, u00118, "system__val_lliB");
   u00119 : constant Version_32 := 16#0a0077b1#;
   pragma Export (C, u00119, "system__val_lliS");
   u00120 : constant Version_32 := 16#1c33b50d#;
   pragma Export (C, u00120, "types_baseB");
   u00121 : constant Version_32 := 16#447091ce#;
   pragma Export (C, u00121, "types_baseS");
   u00122 : constant Version_32 := 16#5b838a95#;
   pragma Export (C, u00122, "pseudo_code__tableB");
   u00123 : constant Version_32 := 16#6cd79f5c#;
   pragma Export (C, u00123, "pseudo_code__tableS");
   u00124 : constant Version_32 := 16#12c24a43#;
   pragma Export (C, u00124, "ada__charactersS");
   u00125 : constant Version_32 := 16#239b1678#;
   pragma Export (C, u00125, "ada__characters__handlingB");
   u00126 : constant Version_32 := 16#313e5d46#;
   pragma Export (C, u00126, "ada__characters__handlingS");
   u00127 : constant Version_32 := 16#4b7bb96a#;
   pragma Export (C, u00127, "ada__characters__latin_1S");
   u00128 : constant Version_32 := 16#af50e98f#;
   pragma Export (C, u00128, "ada__stringsS");
   u00129 : constant Version_32 := 16#96e9c1e7#;
   pragma Export (C, u00129, "ada__strings__mapsB");
   u00130 : constant Version_32 := 16#6a512c5d#;
   pragma Export (C, u00130, "ada__strings__mapsS");
   u00131 : constant Version_32 := 16#431dd077#;
   pragma Export (C, u00131, "system__bit_opsB");
   u00132 : constant Version_32 := 16#0765e3a3#;
   pragma Export (C, u00132, "system__bit_opsS");
   u00133 : constant Version_32 := 16#34090881#;
   pragma Export (C, u00133, "ada__strings__maps__constantsS");
   u00134 : constant Version_32 := 16#5b9edcc4#;
   pragma Export (C, u00134, "system__compare_array_unsigned_8B");
   u00135 : constant Version_32 := 16#3927e09c#;
   pragma Export (C, u00135, "system__compare_array_unsigned_8S");
   u00136 : constant Version_32 := 16#5f72f755#;
   pragma Export (C, u00136, "system__address_operationsB");
   u00137 : constant Version_32 := 16#83282f22#;
   pragma Export (C, u00137, "system__address_operationsS");
   u00138 : constant Version_32 := 16#fd83e873#;
   pragma Export (C, u00138, "system__concat_2B");
   u00139 : constant Version_32 := 16#928446c1#;
   pragma Export (C, u00139, "system__concat_2S");
   u00140 : constant Version_32 := 16#c7fe82f1#;
   pragma Export (C, u00140, "reel_esB");
   u00141 : constant Version_32 := 16#bbe3e6eb#;
   pragma Export (C, u00141, "reel_esS");
   u00142 : constant Version_32 := 16#d5f9759f#;
   pragma Export (C, u00142, "ada__text_io__float_auxB");
   u00143 : constant Version_32 := 16#f854caf5#;
   pragma Export (C, u00143, "ada__text_io__float_auxS");
   u00144 : constant Version_32 := 16#56e74f1a#;
   pragma Export (C, u00144, "system__img_realB");
   u00145 : constant Version_32 := 16#578cc0f3#;
   pragma Export (C, u00145, "system__img_realS");
   u00146 : constant Version_32 := 16#4f1f4f21#;
   pragma Export (C, u00146, "system__fat_llfS");
   u00147 : constant Version_32 := 16#1b28662b#;
   pragma Export (C, u00147, "system__float_controlB");
   u00148 : constant Version_32 := 16#70d8d22d#;
   pragma Export (C, u00148, "system__float_controlS");
   u00149 : constant Version_32 := 16#3da6be5a#;
   pragma Export (C, u00149, "system__img_lluB");
   u00150 : constant Version_32 := 16#88eb037d#;
   pragma Export (C, u00150, "system__img_lluS");
   u00151 : constant Version_32 := 16#22ab03a2#;
   pragma Export (C, u00151, "system__img_unsB");
   u00152 : constant Version_32 := 16#5ed63f49#;
   pragma Export (C, u00152, "system__img_unsS");
   u00153 : constant Version_32 := 16#c054f766#;
   pragma Export (C, u00153, "system__powten_tableS");
   u00154 : constant Version_32 := 16#8ff77155#;
   pragma Export (C, u00154, "system__val_realB");
   u00155 : constant Version_32 := 16#6e0de600#;
   pragma Export (C, u00155, "system__val_realS");
   u00156 : constant Version_32 := 16#0be1b996#;
   pragma Export (C, u00156, "system__exn_llfB");
   u00157 : constant Version_32 := 16#11a08ffe#;
   pragma Export (C, u00157, "system__exn_llfS");
   u00158 : constant Version_32 := 16#35cfe1cb#;
   pragma Export (C, u00158, "system__fat_lfltS");
   --  BEGIN ELABORATION ORDER
   --  ada%s
   --  ada.characters%s
   --  ada.characters.handling%s
   --  ada.characters.latin_1%s
   --  interfaces%s
   --  system%s
   --  system.address_operations%s
   --  system.address_operations%b
   --  system.case_util%s
   --  system.case_util%b
   --  system.exn_llf%s
   --  system.exn_llf%b
   --  system.float_control%s
   --  system.float_control%b
   --  system.htable%s
   --  system.img_bool%s
   --  system.img_bool%b
   --  system.img_enum_new%s
   --  system.img_enum_new%b
   --  system.img_int%s
   --  system.img_int%b
   --  system.img_lli%s
   --  system.img_lli%b
   --  system.img_real%s
   --  system.io%s
   --  system.io%b
   --  system.parameters%s
   --  system.parameters%b
   --  system.crtl%s
   --  interfaces.c_streams%s
   --  interfaces.c_streams%b
   --  system.powten_table%s
   --  system.standard_library%s
   --  system.exceptions_debug%s
   --  system.exceptions_debug%b
   --  system.storage_elements%s
   --  system.storage_elements%b
   --  system.stack_checking%s
   --  system.stack_checking%b
   --  system.string_hash%s
   --  system.string_hash%b
   --  system.htable%b
   --  system.strings%s
   --  system.strings%b
   --  system.os_lib%s
   --  system.traceback_entries%s
   --  system.traceback_entries%b
   --  ada.exceptions%s
   --  system.soft_links%s
   --  system.unsigned_types%s
   --  system.fat_lflt%s
   --  system.fat_llf%s
   --  system.img_biu%s
   --  system.img_biu%b
   --  system.img_llb%s
   --  system.img_llb%b
   --  system.img_llu%s
   --  system.img_llu%b
   --  system.img_llw%s
   --  system.img_llw%b
   --  system.img_uns%s
   --  system.img_uns%b
   --  system.img_real%b
   --  system.img_wiu%s
   --  system.img_wiu%b
   --  system.val_int%s
   --  system.val_lli%s
   --  system.val_llu%s
   --  system.val_real%s
   --  system.val_uns%s
   --  system.val_util%s
   --  system.val_util%b
   --  system.val_uns%b
   --  system.val_real%b
   --  system.val_llu%b
   --  system.val_lli%b
   --  system.val_int%b
   --  system.wch_con%s
   --  system.wch_con%b
   --  system.wch_cnv%s
   --  system.wch_jis%s
   --  system.wch_jis%b
   --  system.wch_cnv%b
   --  system.wch_stw%s
   --  system.wch_stw%b
   --  ada.exceptions.last_chance_handler%s
   --  ada.exceptions.last_chance_handler%b
   --  system.address_image%s
   --  system.bit_ops%s
   --  system.bit_ops%b
   --  system.compare_array_unsigned_8%s
   --  system.compare_array_unsigned_8%b
   --  system.concat_2%s
   --  system.concat_2%b
   --  system.exception_table%s
   --  system.exception_table%b
   --  ada.io_exceptions%s
   --  ada.strings%s
   --  ada.strings.maps%s
   --  ada.strings.maps.constants%s
   --  ada.tags%s
   --  ada.streams%s
   --  ada.streams%b
   --  interfaces.c%s
   --  system.exceptions%s
   --  system.exceptions%b
   --  system.exceptions.machine%s
   --  system.finalization_root%s
   --  system.finalization_root%b
   --  ada.finalization%s
   --  ada.finalization%b
   --  system.storage_pools%s
   --  system.storage_pools%b
   --  system.finalization_masters%s
   --  system.storage_pools.subpools%s
   --  system.storage_pools.subpools.finalization%s
   --  system.storage_pools.subpools.finalization%b
   --  system.memory%s
   --  system.memory%b
   --  system.standard_library%b
   --  system.pool_global%s
   --  system.pool_global%b
   --  system.file_control_block%s
   --  system.file_io%s
   --  system.secondary_stack%s
   --  system.file_io%b
   --  system.storage_pools.subpools%b
   --  system.finalization_masters%b
   --  interfaces.c%b
   --  ada.tags%b
   --  ada.strings.maps%b
   --  system.soft_links%b
   --  system.os_lib%b
   --  ada.characters.handling%b
   --  system.secondary_stack%b
   --  system.address_image%b
   --  system.traceback%s
   --  ada.exceptions%b
   --  system.traceback%b
   --  ada.text_io%s
   --  ada.text_io%b
   --  ada.text_io.float_aux%s
   --  ada.text_io.generic_aux%s
   --  ada.text_io.generic_aux%b
   --  ada.text_io.float_aux%b
   --  ada.text_io.integer_aux%s
   --  ada.text_io.integer_aux%b
   --  types_base%s
   --  types_base%b
   --  entier_es%s
   --  entier_es%b
   --  pseudo_code%s
   --  test_pseudo_code%b
   --  reel_es%s
   --  reel_es%b
   --  pseudo_code.table%s
   --  pseudo_code.table%b
   --  pseudo_code%b
   --  END ELABORATION ORDER


end ada_main;
