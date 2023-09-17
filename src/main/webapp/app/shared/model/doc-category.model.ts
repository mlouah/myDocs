import { IDoc } from '@/shared/model/doc.model';

export interface IDocCategory {
  id?: number;
  name?: string | null;
  code?: string | null;
  notes?: string | null;
  docs?: IDoc[] | null;
}

export class DocCategory implements IDocCategory {
  constructor(
    public id?: number,
    public name?: string | null,
    public code?: string | null,
    public notes?: string | null,
    public docs?: IDoc[] | null
  ) {}
}
