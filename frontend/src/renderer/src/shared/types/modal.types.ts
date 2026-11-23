
// modal interface
export interface iModal{
   title: string; 
   msg: string; 
   btt_event: boolean | string; 
   btt_close: boolean | string;
   btt_close_class?: boolean;
   display: boolean; 
   onClose: () => void; 
   modalEvent?: (event: unknown) => void;
};


// modal config interface
export interface iModalConfig{
   title: string;
   msg: string;
   btt_event: boolean | string;
   btt_close: boolean | string;
   display: boolean;
};