pragma Ada_95;
pragma Source_File_Name (ada_main, Spec_File_Name => "b~test_pseudo_code.ads");
pragma Source_File_Name (ada_main, Body_File_Name => "b~test_pseudo_code.adb");
with Ada.Exceptions;

package body ada_main is
   pragma Warnings (Off);

   E074 : Short_Integer; pragma Import (Ada, E074, "system__os_lib_E");
   E013 : Short_Integer; pragma Import (Ada, E013, "system__soft_links_E");
   E158 : Short_Integer; pragma Import (Ada, E158, "system__fat_lflt_E");
   E146 : Short_Integer; pragma Import (Ada, E146, "system__fat_llf_E");
   E023 : Short_Integer; pragma Import (Ada, E023, "system__exception_table_E");
   E050 : Short_Integer; pragma Import (Ada, E050, "ada__io_exceptions_E");
   E128 : Short_Integer; pragma Import (Ada, E128, "ada__strings_E");
   E130 : Short_Integer; pragma Import (Ada, E130, "ada__strings__maps_E");
   E133 : Short_Integer; pragma Import (Ada, E133, "ada__strings__maps__constants_E");
   E052 : Short_Integer; pragma Import (Ada, E052, "ada__tags_E");
   E049 : Short_Integer; pragma Import (Ada, E049, "ada__streams_E");
   E070 : Short_Integer; pragma Import (Ada, E070, "interfaces__c_E");
   E029 : Short_Integer; pragma Import (Ada, E029, "system__exceptions_E");
   E068 : Short_Integer; pragma Import (Ada, E068, "system__finalization_root_E");
   E066 : Short_Integer; pragma Import (Ada, E066, "ada__finalization_E");
   E087 : Short_Integer; pragma Import (Ada, E087, "system__storage_pools_E");
   E079 : Short_Integer; pragma Import (Ada, E079, "system__finalization_masters_E");
   E093 : Short_Integer; pragma Import (Ada, E093, "system__storage_pools__subpools_E");
   E089 : Short_Integer; pragma Import (Ada, E089, "system__pool_global_E");
   E077 : Short_Integer; pragma Import (Ada, E077, "system__file_control_block_E");
   E064 : Short_Integer; pragma Import (Ada, E064, "system__file_io_E");
   E017 : Short_Integer; pragma Import (Ada, E017, "system__secondary_stack_E");
   E006 : Short_Integer; pragma Import (Ada, E006, "ada__text_io_E");
   E121 : Short_Integer; pragma Import (Ada, E121, "types_base_E");
   E097 : Short_Integer; pragma Import (Ada, E097, "pseudo_code_E");
   E123 : Short_Integer; pragma Import (Ada, E123, "pseudo_code__table_E");

   Local_Priority_Specific_Dispatching : constant String := "";
   Local_Interrupt_States : constant String := "";

   Is_Elaborated : Boolean := False;

   procedure finalize_library is
   begin
      E006 := E006 - 1;
      declare
         procedure F1;
         pragma Import (Ada, F1, "ada__text_io__finalize_spec");
      begin
         F1;
      end;
      E079 := E079 - 1;
      E093 := E093 - 1;
      declare
         procedure F2;
         pragma Import (Ada, F2, "system__file_io__finalize_body");
      begin
         E064 := E064 - 1;
         F2;
      end;
      declare
         procedure F3;
         pragma Import (Ada, F3, "system__file_control_block__finalize_spec");
      begin
         E077 := E077 - 1;
         F3;
      end;
      E089 := E089 - 1;
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
      E158 := E158 + 1;
      System.Fat_Llf'Elab_Spec;
      E146 := E146 + 1;
      System.Exception_Table'Elab_Body;
      E023 := E023 + 1;
      Ada.Io_Exceptions'Elab_Spec;
      E050 := E050 + 1;
      Ada.Strings'Elab_Spec;
      E128 := E128 + 1;
      Ada.Strings.Maps'Elab_Spec;
      Ada.Strings.Maps.Constants'Elab_Spec;
      E133 := E133 + 1;
      Ada.Tags'Elab_Spec;
      Ada.Streams'Elab_Spec;
      E049 := E049 + 1;
      Interfaces.C'Elab_Spec;
      System.Exceptions'Elab_Spec;
      E029 := E029 + 1;
      System.Finalization_Root'Elab_Spec;
      E068 := E068 + 1;
      Ada.Finalization'Elab_Spec;
      E066 := E066 + 1;
      System.Storage_Pools'Elab_Spec;
      E087 := E087 + 1;
      System.Finalization_Masters'Elab_Spec;
      System.Storage_Pools.Subpools'Elab_Spec;
      System.Pool_Global'Elab_Spec;
      E089 := E089 + 1;
      System.File_Control_Block'Elab_Spec;
      E077 := E077 + 1;
      System.File_Io'Elab_Body;
      E064 := E064 + 1;
      E093 := E093 + 1;
      System.Finalization_Masters'Elab_Body;
      E079 := E079 + 1;
      E070 := E070 + 1;
      Ada.Tags'Elab_Body;
      E052 := E052 + 1;
      E130 := E130 + 1;
      System.Soft_Links'Elab_Body;
      E013 := E013 + 1;
      System.Os_Lib'Elab_Body;
      E074 := E074 + 1;
      System.Secondary_Stack'Elab_Body;
      E017 := E017 + 1;
      Ada.Text_Io'Elab_Spec;
      Ada.Text_Io'Elab_Body;
      E006 := E006 + 1;
      E121 := E121 + 1;
      Pseudo_Code'Elab_Spec;
      Pseudo_Code.Table'Elab_Spec;
      Pseudo_Code.Table'Elab_Body;
      E123 := E123 + 1;
      Pseudo_Code'Elab_Body;
      E097 := E097 + 1;
   end adainit;

   procedure Ada_Main_Program;
   pragma Import (Ada, Ada_Main_Program, "_ada_test_pseudo_code");

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
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/types_base.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/entier_es.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/test_pseudo_code.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/reel_es.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/pseudo_code-table.o
   --   /home/francesco/Desktop/Projet CS444/Interp_MA/Obj/pseudo_code.o
   --   -L/home/francesco/Desktop/Projet CS444/Interp_MA/Obj/
   --   -L../Obj/
   --   -L/usr/lib/gcc/x86_64-linux-gnu/4.9/adalib/
   --   -shared
   --   -lgnat-4.9
--  END Object file/option list   

end ada_main;
