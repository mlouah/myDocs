import { IDoc } from '@/shared/model/doc.model';

export interface ILanguage {
  id?: number;
  name?: string | null;
  code?: string | null;
  docs?: IDoc[] | null;
}

export class Language implements ILanguage {
  constructor(public id?: number, public name?: string | null, public code?: string | null, public docs?: IDoc[] | null) {}
}
