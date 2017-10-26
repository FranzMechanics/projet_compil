pragma Ada_95;
pragma Source_File_Name (ada_main, Spec_File_Name => "b~ima.ads");
pragma Source_File_Name (ada_main, Body_File_Name => "b~ima.adb");
with Ada.Exceptions;

package body ada_main is
   pragma Warnings (Off);

   E078 : Short_Integer; pragma Import (Ada, E078, "system__os_lib_E");
   E013 : Short_Integer; pragma Import (Ada, E013, "system__soft_links_E");
   E169 : Short_Integer; pragma Import (Ada, E169, "system__fat_lflt_E");
   E157 : Short_Integer; pragma Import (Ada, E157, "system__fat_llf_E");
   E019 : Short_Integer; pragma Import (Ada, E019, "system__exception_table_E");
   E054 : Short_Integer; pragma Import (Ada, E054, "ada__io_exceptions_E");
   E145 : Short_Integer; pragma Import (Ada, E145, "ada__strings_E");
   E147 : Short_Integer; pragma Import (Ada, E147, "ada__strings__maps_E");
   E150 : Short_Integer; pragma Import (Ada, E150, "ada__strings__maps__constants_E");
   E056 : Short_Integer; pragma Import (Ada, E056, "ada__tags_E");
   E053 : Short_Integer; pragma Import (Ada, E053, "ada__streams_E");
   E074 : Short_Integer; pragma Import (Ada, E074, "interfaces__c_E");
   E025 : Short_Integer; pragma Import (Ada, E025, "system__exceptions_E");
   E072 : Short_Integer; pragma Import (Ada, E072, "system__finalization_root_E");
   E070 : Short_Integer; pragma Import (Ada, E070, "ada__finalization_E");
   E091 : Short_Integer; pragma Import (Ada, E091, "system__storage_pools_E");
   E083 : Short_Integer; pragma Import (Ada, E083, "system__finalization_masters_E");
   E097 : Short_Integer; pragma Import (Ada, E097, "system__storage_pools__subpools_E");
   E093 : Short_Integer; pragma Import (Ada, E093, "system__pool_global_E");
   E081 : Short_Integer; pragma Import (Ada, E081, "system__file_control_block_E");
   E068 : Short_Integer; pragma Import (Ada, E068, "system__file_io_E");
   E009 : Short_Integer; pragma Import (Ada, E009, "system__secondary_stack_E");
   E051 : Short_Integer; pragma Import (Ada, E051, "ada__text_io_E");
   E182 : Short_Integer; pragma Import (Ada, E182, "ma_lexico_dfa_E");
   E184 : Short_Integer; pragma Import (Ada, E184, "ma_lexico_io_E");
   E198 : Short_Integer; pragma Import (Ada, E198, "options_E");
   E128 : Short_Integer; pragma Import (Ada, E128, "types_base_E");
   E196 : Short_Integer; pragma Import (Ada, E196, "lecture_entiers_E");
   E202 : Short_Integer; pragma Import (Ada, E202, "lecture_reels_E");
   E125 : Short_Integer; pragma Import (Ada, E125, "mes_tables_E");
   E136 : Short_Integer; pragma Import (Ada, E136, "pseudo_code_E");
   E121 : Short_Integer; pragma Import (Ada, E121, "assembleur_E");
   E123 : Short_Integer; pragma Import (Ada, E123, "ma_detiq_E");
   E178 : Short_Integer; pragma Import (Ada, E178, "ma_syntax_tokens_E");
   E173 : Short_Integer; pragma Import (Ada, E173, "ma_dict_E");
   E171 : Short_Integer; pragma Import (Ada, E171, "ma_lexico_E");
   E188 : Short_Integer; pragma Import (Ada, E188, "ma_syntax_E");
   E200 : Short_Integer; pragma Import (Ada, E200, "partie_op_E");
   E140 : Short_Integer; pragma Import (Ada, E140, "pseudo_code__table_E");

   Local_Priority_Specific_Dispatching : constant String := "";
   Local_Interrupt_States : constant String := "";

   Is_Elaborated : Boolean := False;

   procedure finalize_library is
   begin
      E051 := E051 - 1;
      declare
         procedure F1;
         pragma Import (Ada, F1, "ada__text_io__finalize_spec");
      begin
         F1;
      end;
      E083 := E083 - 1;
      E097 := E097 - 1;
      declare
         procedure F2;
         pragma Import (Ada, F2, "system__file_io__finalize_body");
      begin
         E068 := E068 - 1;
         F2;
      end;
      declare
         procedure F3;
         pragma Import (Ada, F3, "system__file_control_block__finalize_spec");
      begin
         E081 := E081 - 1;
         F3;
      end;
      E093 := E093 - 1;
      declare
         procedure F4;
         pragma Import (Ada, F4, "system__pool_global__finalize_spec");
      begin
         F4;
      end;
      declare
         procedure F5;
         pragma Import (Ada, F5, "system__storage_pools__subpools__finalize_spec");
      begin
         F5;
      end;
      declare
         procedure F6;
         pragma Import (Ada, F6, "system__finalization_masters__finalize_spec");
      begin
         F6;
      end;
      declare
         procedure Reraise_Library_Exception_If_Any;
            pragma Import (Ada, Reraise_Library_Exception_If_Any, "__gnat_reraise_library_exception_if_any");
      begin
         Reraise_Library_Exception_If_Any;
      end;
   end finalize_library;

   procedure adafinal is
      procedure s_stalib_adafinal;
      pragma Import (C, s_stalib_adafinal, "system__standard_library__adafinal");
   begin
      if not Is_Elaborated then
         return;
      end if;
      Is_Elaborated := False;
      s_stalib_adafinal;
   end adafinal;

   type No_Param_Proc is access procedure;

   procedure adainit is
      Main_Priority : Integer;
      pragma Import (C, Main_Priority, "__gl_main_priority");
      Time_Slice_Value : Integer;
      pragma Import (C, Time_Slice_Value, "__gl_time_slice_val");
      WC_Encoding : Character;
      pragma Import (C, WC_Encoding, "__gl_wc_encoding");
      Locking_Policy : Character;
      pragma Import (C, Locking_Policy, "__gl_locking_policy");
      Queuing_Policy : Character;
      pragma Import (C, Queuing_Policy, "__gl_queuing_policy");
      Task_Dispatching_Policy : Character;
      pragma Import (C, Task_Dispatching_Policy, "__gl_task_dispatching_policy");
      Priority_Specific_Dispatching : System.Address;
      pragma Import (C, Priority_Specific_Dispatching, "__gl_priority_specific_dispatching");
      Num_Specific_Dispatching : Integer;
      pragma Import (C, Num_Specific_Dispatching, "__gl_num_specific_dispatching");
      Main_CPU : Integer;
      pragma Import (C, Main_CPU, "__gl_main_cpu");
      Interrupt_States : System.Address;
      pragma Import (C, Interrupt_States, "__gl_interrupt_states");
      Num_Interrupt_States : Integer;
      pragma Import (C, Num_Interrupt_States, "__gl_num_interrupt_states");
      Unreserve_All_Interrupts : Integer;
      pragma Import (C, Unreserve_All_Interrupts, "__gl_unreserve_all_interrupts");
      Detect_Blocking : Integer;
      pragma Import (C, Detect_Blocking, "__gl_detect_blocking");
      Default_Stack_Size : Integer;
      pragma Import (C, Default_Stack_Size, "__gl_default_stack_size");
      Leap_Seconds_Support : Integer;
      pragma Import (C, Leap_Seconds_Support, "__gl_leap_seconds_support");

      procedure Install_Handler;
      pragma Import (C, Install_Handler, "__gnat_install_handler");

      Handler_Installed : Integer;
      pragma Import (C, Handler_Installed, "__gnat_handler_installed");

      Finalize_Library_Objects : No_Param_Proc;
      pragma Import (C, Finalize_Library_Objects, "__gnat_finalize_library_objects");
   begin
      if Is_Elaborated then
         return;
      end if;
      Is_Elaborated := True;
      Main_Priority := -1;
      Time_Slice_Value := -1;
      WC_Encoding := 'b';
      Locking_Policy := ' ';
      Queuing_Policy := ' ';
      Task_Dispatching_Policy := ' ';
      Priority_Specific_Dispatching :=
        Local_Priority_Specific_Dispatching'Address;
      Num_Specific_Dispatching := 0;
      Main_CPU := -1;
      Interrupt_States := Local_Interrupt_States'Address;
      Num_Interrupt_States := 0;
      Unreserve_All_Interrupts := 0;
      Detect_Blocking := 0;
      Default_Stack_Size := -1;
      Leap_Seconds_Support := 0;

      if Handler_Installed = 0 then
         Install_Handler;
      end if;

      Finalize_Library_Objects := finalize_library'access;

      System.Soft_Links'Elab_Spec;
      System.Fat_Lflt'Elab_Spec;
      E169 := E169 + 1;
      System.Fat_Llf'Elab_Spec;
      E157 := E157 + 1;
      System.Exception_Table'Elab_Body;
      E019 := E019 + 1;
      Ada.Io_Exceptions'Elab_Spec;
      E054 := E054 + 1;
      Ada.Strings'Elab_Spec;
      E145 := E145 + 1;
      Ada.Strings.Maps'Elab_Spec;
      Ada.Strings.Maps.Constants'Elab_Spec;
      E150 := E150 + 1;
      Ada.Tags'Elab_Spec;
      Ada.Streams'Elab_Spec;
      E053 := E053 + 1;
      Interfaces.C'Elab_Spec;
      System.Exceptions'Elab_Spec;
      E025 := E025 + 1;
      System.Finalization_Root'Elab_Spec;
      E072 := E072 + 1;
      Ada.Finalization'Elab_Spec;
      E070 := E070 + 1;
      System.Storage_Pools'Elab_Spec;
      E091 := E091 + 1;
      System.Finalization_Masters'Elab_Spec;
      System.Storage_Pools.Subpools'Elab_Spec;
      System.Pool_Global'Elab_Spec;
      E093 := E093 + 1;
      System.File_Control_Block'Elab_Spec;
      E081 := E081 + 1;
      System.File_Io'Elab_Body;
      E068 := E068 + 1;
      E097 := E097 + 1;
      System.Finalization_Masters'Elab_Body;
      E083 := E083 + 1;
      E074 := E074 + 1;
      Ada.Tags'Elab_Body;
      E056 := E056 + 1;
      E147 := E147 + 1;
      System.Soft_Links'Elab_Body;
      E013 := E013 + 1;
      System.Os_Lib'Elab_Body;
      E078 := E078 + 1;
      System.Secondary_Stack'Elab_Body;
      E009 := E009 + 1;
      Ada.Text_Io'Elab_Spec;
      Ada.Text_Io'Elab_Body;
      E051 := E051 + 1;
      E182 := E182 + 1;
      MA_LEXICO_IO'ELAB_SPEC;
      E184 := E184 + 1;
      OPTIONS'ELAB_SPEC;
      E198 := E198 + 1;
      E128 := E128 + 1;
      E196 := E196 + 1;
      E125 := E125 + 1;
      Pseudo_Code'Elab_Spec;
      ASSEMBLEUR'ELAB_SPEC;
      MA_DETIQ'ELAB_SPEC;
      MA_DETIQ'ELAB_BODY;
      E123 := E123 + 1;
      Ma_Syntax_Tokens'Elab_Spec;
      E178 := E178 + 1;
      MA_LEXICO'ELAB_SPEC;
      E188 := E188 + 1;
      ASSEMBLEUR'ELAB_BODY;
      E121 := E121 + 1;
      MA_DICT'ELAB_BODY;
      E173 := E173 + 1;
      PARTIE_OP'ELAB_SPEC;
      PARTIE_OP'ELAB_BODY;
      E200 := E200 + 1;
      E202 := E202 + 1;
      Pseudo_Code.Table'Elab_Spec;
      Pseudo_Code.Table'Elab_Body;
      E140 := E140 + 1;
      Pseudo_Code'Elab_Body;
      E136 := E136 + 1;
      MA_LEXICO'ELAB_BODY;
      E171 := E171 + 1;
   end adainit;

   procedure Ada_Main_Program;
   pragma Import (Ada, Ada_Main_Program, "_ada_ima");

   function main
     (argc : Integer;
      argv : System.Address;
      envp : System.Address)
      return Integer
   is
      procedure Initialize (Addr : System.Address);
      pragma Import (C, Initialize, "__gnat_initialize");

      procedure Finalize;
      pragma Import (C, Finalize, "__gnat_finalize");
      SEH : aliased array (1 .. 2) of Integer;

      Ensure_Reference : aliased System.Address := Ada_Main_Program_Name'Address;
      pragma Volatile (Ensure_Reference);

   begin
      gnat_argc := argc;
      gnat_argv := argv;
      gnat_envp := envp;

      Initialize (SEH'Address);
      adainit;
      Ada_Main_Program;
      adafinal;
      Finalize;
      return (gnat_exit_status);
   end;

--  BEGIN Object file/option list
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/ma_lexico_dfa.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/ma_lexico_io.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/ma_syntax_goto.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/ma_syntax_shift_reduce.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/options.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/types_base.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/entier_es.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/lecture_entiers.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/mes_tables.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/ma_detiq.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/ma_syntax_tokens.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/ma_syntax.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/assembleur.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/ma_token_io.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/ma_dict.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/ima.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/reel_es.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/partie_op.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/lecture_reels.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/pseudo_code-table.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/pseudo_code.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/ma_lexico.o
   --   -L/home/francesco/Desktop/Projet CS444/Interp_MA/Obj/
   --   -L../Obj/
   --   -L/usr/lib/gcc/x86_64-linux-gnu/4.9/adalib/
   --   -shared
   --   -lgnat-4.9
--  END Object file/option list   

end ada_main;
