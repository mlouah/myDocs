import { IDoc } from '@/shared/model/doc.model';

export interface IDocFormat {
  id?: number;
  format?: string | null;
  code?: string | null;
  notes?: string | null;
  docs?: IDoc[] | null;
}

export class DocFormat implements IDocFormat {
  constructor(
    public id?: number,
    public format?: string | null,
    public code?: string | null,
    public notes?: string | null,
    public docs?: IDoc[] | null
  ) {}
}
